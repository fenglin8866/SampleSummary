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
        // restore()
    }

    // =========================
    // 下载操作
    // =========================

    fun start(app: AppInfo) {
        val current = _stateFlow.value[app.packageName]
        if (current?.status == Status.DOWNLOADING) return

        sdk.download(app.packageName, callback(app.packageName))

        update(app.packageName) {
            it.copy(status = Status.DOWNLOADING)
        }
    }

    fun pause(packageName: String) {
        sdk.pause(packageName)
        update(packageName) { it.copy(status = Status.PAUSED) }
    }

    fun resume(packageName: String) {
        sdk.resume(packageName)
        update(packageName) { it.copy(status = Status.DOWNLOADING) }
    }

    // =========================
    // 回调处理
    // =========================

    private fun callback(packageName: String) = object : DownloadCallback {

        override fun onProgress(progress: Int) {
            update(packageName) {
                it.copy(progress = progress, status = Status.DOWNLOADING)
            }
        }

        override fun onStatusChanged(status: Status) {
            if (status == Status.SUCCESS) {
                update(packageName) {
                    it.copy(progress = 100, status = Status.SUCCESS)
                }
                deleteFromDB(packageName)
            } else {
                update(packageName) {
                    it.copy(status = status)
                }
            }
        }

    }

    // =========================
    // 状态更新
    // =========================

    private fun update(
        packageName: String,
        block: (DownloadState) -> DownloadState
    ) {
        scope.launch {
            val map = _stateFlow.value.toMutableMap()
            val old = map[packageName] ?: DownloadState(packageName)
            val newState = block(old)
            map[packageName] = newState
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
                    packageName = state.packageName,
                    status = state.status.ordinal,
                    progress = state.progress,
                    updateTime = now
                )
            )
        }
    }

    private fun deleteFromDB(packageName: String) {
        scope.launch {
            dao.delete(packageName)
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
                it.packageName to DownloadState(
                    packageName = it.packageName,
                    progress = it.progress,
                    status = Status.entries[it.status]
                )
            }.toMutableMap()

            // 2. 从 SDK 同步
            val sdkTasks = sdk.getDownloadingList()
            sdkTasks.forEach {
                map[it.packageName] = DownloadState(
                    packageName = it.packageName,
                    progress = it.progress,
                    status = Status.DOWNLOADING
                )
            }

            _stateFlow.emit(map)
        }
    }
}