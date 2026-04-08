package com.xxh.summary.download.data.repository

import android.content.Context
import android.util.Log
import com.xxh.summary.download.data.db.DownloadDao
import com.xxh.summary.download.data.db.DownloadTaskEntity
import com.xxh.summary.download.model.DownloadState
import com.xxh.summary.download.model.Status
import com.xxh.summary.download.sdk.AppCenterSdk
import com.xxh.summary.download.sdk.DownloadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object DownloadRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _stateFlow = MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    val stateFlow: StateFlow<Map<String, DownloadState>> =
        _stateFlow.asStateFlow()

    private lateinit var sdk: AppCenterSdk
    private lateinit var dao: DownloadDao

    fun init(
        context: Context,
        sdk: AppCenterSdk,
        dao: DownloadDao
    ) {
        this.sdk = sdk
        this.dao = dao
        sdk.initSdk(context) { isSuccess ->
            if (isSuccess) {
                restore()
            }
        }
    }



    // =========================
    // 下载操作
    // =========================

    fun start(packageName: String) {
        val current = _stateFlow.value[packageName]
        if (current?.status == Status.DOWNLOADING) return

        sdk.download(packageName, callback(packageName))

        /*update(packageName) {
            it.copy(status = Status.DOWNLOADING)
        }*/
    }

    fun pause(packageName: String) {
        Log.i("xxh222", "pause $packageName")
        sdk.pause(packageName) { state ->
            update(packageName) { it.copy(progress = state.progress, status = state.status) }
        }
    }

    fun resume(packageName: String) {
        sdk.resume(packageName) { state ->
            update(packageName) { it.copy(progress = state.progress, status = state.status) }
        }
        // update(packageName) { it.copy(status = Status.DOWNLOADING) }
    }

    fun open(packageName: String) {
        sdk.open(packageName)
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
               // deleteFromDB(packageName)
            } else {
                var progress: Int? = null
                if (status == Status.IDLE) {
                    progress = 0
                }
                update(packageName) {
                    it.copy(status = status, progress = progress ?: it.progress)
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

            saveToDB(newState)
        }
    }

    private var lastSaveTime = 0L

    private fun saveToDB(state: DownloadState) {
        val now = System.currentTimeMillis()
        // 节流，避免频繁保存
        if (state.status == Status.DOWNLOADING) {
            if (now - lastSaveTime < 1000) return
            lastSaveTime = now
        }
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
            /*val sdkTasks = sdk.getDownloadingList()
            sdkTasks.forEach {
                map[it.packageName] = DownloadState(
                    packageName = it.packageName,
                    progress = it.progress,
                    status = Status.DOWNLOADING
                )
            }*/

            map.forEach { (packageName, state) ->
                Log.i("xxh333", "${packageName}: $state")
                var restoreState = state
                val remoteState = sdk.queryDownloadStatus(packageName)
                if (remoteState != null) {
                    restoreState = remoteState
                    map[packageName] = restoreState
                    saveToDB(restoreState)
                }
                // 冷启动恢复没有回调，需要手动注册
                sdk.registerCallback(restoreState, callback(packageName))
            }

            _stateFlow.emit(map)
        }
    }
}