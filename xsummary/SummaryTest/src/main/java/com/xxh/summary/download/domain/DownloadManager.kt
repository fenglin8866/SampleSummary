package com.xxh.summary.download.domain

import com.xxh.summary.download.data.db.DownloadDao
import com.xxh.summary.download.data.db.DownloadTaskEntity
import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.data.model.DownloadState
import com.xxh.summary.download.data.model.Status
import com.xxh.summary.download.sdk.AppCenterSdk
import com.xxh.summary.download.sdk.DownloadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object DownloadManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _stateFlow =
        MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    val stateFlow: StateFlow<Map<String, DownloadState>> =
        _stateFlow.asStateFlow()

    private lateinit var sdk: AppCenterSdk
    private lateinit var dao: DownloadDao

    fun init(
        sdk: AppCenterSdk,
        dao: DownloadDao
    ) {
        this.sdk = sdk
        this.dao = dao
        restore()
    }

    // =========================
    // 下载操作
    // =========================

    fun start(app: AppInfo) {
        val current = _stateFlow.value[app.appId]
        if (current?.status == Status.DOWNLOADING) return

        sdk.download(app.appId, app.downloadUrl, callback(app.appId))

        update(app.appId) {
            it.copy(status = Status.DOWNLOADING)
        }
    }

    fun pause(appId: String) {
        sdk.pause(appId)
        update(appId) { it.copy(status = Status.PAUSED) }
    }

    fun resume(appId: String) {
        sdk.resume(appId)
        update(appId) { it.copy(status = Status.DOWNLOADING) }
    }

    // =========================
    // 回调处理
    // =========================

    private fun callback(appId: String) = object : DownloadCallback {

        override fun onProgress(progress: Int) {
            update(appId) {
                it.copy(progress = progress, status = Status.DOWNLOADING)
            }
        }

        override fun onSuccess() {
            update(appId) {
                it.copy(progress = 100, status = Status.SUCCESS)
            }
            deleteFromDB(appId)
        }

        override fun onFailed() {
            update(appId) {
                it.copy(status = Status.FAILED)
            }
        }
    }

    // =========================
    // 状态更新
    // =========================

    private fun update(
        appId: String,
        block: (DownloadState) -> DownloadState
    ) {
        scope.launch {
            val map = _stateFlow.value.toMutableMap()
            val old = map[appId] ?: DownloadState(appId)
            val newState = block(old)
            map[appId] = newState
            _stateFlow.emit(map)

            saveToDBThrottle(newState)
        }
    }

    // =========================
    // Room 持久化（节流）
    // =========================

    private var lastSaveTime = 0L

    private fun saveToDBThrottle(state: DownloadState) {
        val now = System.currentTimeMillis()
        if (now - lastSaveTime < 1000) return
        lastSaveTime = now

        scope.launch {
            dao.insert(
                DownloadTaskEntity(
                    appId = state.appId,
                    status = state.status.ordinal,
                    progress = state.progress,
                    updateTime = now
                )
            )
        }
    }

    private fun deleteFromDB(appId: String) {
        scope.launch {
            dao.delete(appId)
        }
    }

    // =========================
    // 恢复机制
    // =========================

    fun restore() {
        scope.launch {

            // 1. 从 DB 恢复
            val dbTasks = dao.getAll()
            val map = dbTasks.associate {
                it.appId to DownloadState(
                    appId = it.appId,
                    progress = it.progress,
                    status = Status.values()[it.status]
                )
            }.toMutableMap()

            // 2. 从 SDK 同步
            val sdkTasks = sdk.getDownloadingList()
            sdkTasks.forEach {
                map[it.appId] = DownloadState(
                    appId = it.appId,
                    progress = it.progress,
                    status = Status.DOWNLOADING
                )
            }

            _stateFlow.emit(map)
        }
    }
}