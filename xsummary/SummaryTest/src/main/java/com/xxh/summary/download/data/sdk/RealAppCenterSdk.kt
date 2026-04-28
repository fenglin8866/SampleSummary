package com.xxh.summary.download.data.sdk

import android.content.Context
import android.util.Log
import cn.nubia.ns_lib.ILogger
import cn.nubia.ns_lib.LoggerManager
import cn.nubia.ns_lib.download.BaseNeoDownloadManager
import cn.nubia.ns_lib.download.DownloadListener
import cn.nubia.ns_lib.download.DownloadRequest
import cn.nubia.ns_lib.download.DownloadStatus
import cn.nubia.ns_lib.download.DownloadTask
import cn.nubia.ns_lib.download.InitCallback
import cn.nubia.ns_lib.download.NeoDownloadManager
import cn.nubia.ns_lib.download.Result
import com.xxh.summary.download.model.DownloadState
import com.xxh.summary.download.model.Status
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * 调用接口是耗时操作，不要在主线程调用。
 * 建议：可以增加异步处理，避免阻塞主线程。例如使用协程或者回调。
 */
class RealAppCenterSdk @Inject constructor() : AppCenterSdk {
    private var downloadManager: NeoDownloadManager? = null

    private var isSupportDownload: Boolean = true

    private var ctaStatus: Boolean? = null

    override fun initSdk(context: Context, callback: ((Boolean) -> Unit)?) {
        downloadManager = NeoDownloadManager.getInstance()
        // 2. 初始化服务连接
        downloadManager?.init(context, object : InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "初始化成功")
                checkCTAStatus {
                    ctaStatus = it
                    if (ctaStatus == true) {
                        callback?.invoke(true)
                    }
                }
            }

            override fun onFailure(result: Result?) {
                Log.e(TAG, "初始化失败: ${result?.message}")
                callback?.invoke(false)
            }
        })

        // 3. 设置异常处理器（推荐）
        // Set up exception handlers
        downloadManager?.setConnectionExceptionHandler(object :
            BaseNeoDownloadManager.ConnectionExceptionHandler {
            override fun onServiceDisconnected() {
                Log.d(TAG, "服务连接断开，正在重新连接...")
            }

            override fun onStoreAppNotInstalled() {
                Log.d(TAG, "未安装应用中心")
                isSupportDownload = false
            }

            override fun onStoreAppVersionNotSupported() {
                Log.d(TAG, "应用中心版本过低")
                isSupportDownload = false
            }
        })

        // 4. 自定义日志输出
        LoggerManager.setLogger(object : ILogger {
            override fun d(tag: String, message: String) {
                Log.d(tag, message)
            }

            override fun i(tag: String, message: String) {
                Log.i(tag, message)
            }

            override fun w(tag: String, message: String) {
                Log.w(tag, message)
            }

            override fun e(tag: String, message: String) {
                Log.e(tag, message)
            }

            override fun e(tag: String, message: String, throwable: Throwable) {
                Log.e(tag, message, throwable)
            }
        })
    }

    override fun download(packageName: String, callback: DownloadCallback) {
        val request = DownloadRequest()
        request.packageName = packageName
        request.isAllowMobileNetwork = true
        try {
            downloadManager?.startDownload(
                request,
                getDownloadListener(packageName, callback)
            ) { result ->
                if (NeoDownloadManager.isSuccess(result)) {
                    Log.d(TAG, "开始下载: $packageName")
                } else {
                    Log.d(TAG, "下载启动失败")
                    callback.onStatusChanged(Status.FAILED)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "下载异常", e)
            callback.onStatusChanged(Status.FAILED)
        }
    }

    private fun getLocalProgress(packageName: String): Int {
        return downloadManager?.getLocalDownloadProgress(packageName) ?: 0
    }

    private fun getRemoteProgress(packageName: String, callback: (Int) -> Unit) {
        downloadManager?.getDownloadProgress(packageName) { result ->
            if (NeoDownloadManager.isSuccess(result)) {
                val progress = result.getInt(NeoDownloadManager.RESULT, 0)
                callback(progress)
            } else {
                callback(0)
            }
        }
    }

    suspend fun getRemoteProgress(packageName: String): Int =
        suspendCancellableCoroutine { continuation ->
            downloadManager?.getDownloadProgress(packageName) { result ->
                if (NeoDownloadManager.isSuccess(result)) {
                    val progress = result.getInt(NeoDownloadManager.RESULT, 0)
                    continuation.resume(progress)
                } else {
                    continuation.resume(0)
                }
            }
        }

    private fun getLocalStatus(packageName: String): DownloadStatus {
        val status = downloadManager?.getLocalDownloadStatus(packageName)
        return status ?: DownloadStatus.NOT_STARTED
    }

    /**
     * 暂停异常，不用处理，因为当前是下载中状态，如果处理会进入死循环
     */
    override fun pause(packageName: String, callback: (DownloadState) -> Unit) {
        try {
            downloadManager?.pauseDownload(packageName) { result ->
                Log.d(TAG, "暂停下载: $packageName  result=$result")
                if (NeoDownloadManager.isSuccess(result)) {
                    val downloadState =
                        DownloadState(packageName, getLocalProgress(packageName), Status.PAUSED)
                    callback(downloadState)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "暂停异常", e)
        }
    }

    override fun resume(packageName: String, callback: (DownloadState) -> Unit) {
        try {
            downloadManager?.resumeDownload(packageName) { result ->
                Log.d(TAG, "恢复下载: $packageName result=$result")
                if (NeoDownloadManager.isSuccess(result)) {
                    callback(
                        DownloadState(
                            packageName,
                            getLocalProgress(packageName),
                            Status.DOWNLOADING
                        )
                    )
                } else {
                    callback(
                        DownloadState(
                            packageName,
                            getLocalProgress(packageName),
                            Status.FAILED
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "恢复异常", e)
            callback(DownloadState(packageName, getLocalProgress(packageName), Status.FAILED))
        }
    }

    override fun open(packageName: String) {

    }

    /**
     *        // 获取下载状态
     *             val status = when (statusCode) {
     *                 0 -> "任务不存在"
     *                 1 -> "下载中"
     *                 2 -> "已暂停"
     *                 3 -> "下载成功"
     *                 4 -> "下载失败"
     *                 5 -> "安装中"
     *                 6 -> "安装完成"
     *                 8 -> "未开始"
     *                 9 -> "任务被删除"
     *                 else -> "未知状态"
     *             }
     */
    override fun queryDownloadStatus(packageName: String, callback: (DownloadState?) -> Unit) {
        try {
            downloadManager?.getDownloadStatus(packageName) { result ->
                if (NeoDownloadManager.isSuccess(result)) {
                    val statusCode = result.getInt(NeoDownloadManager.RESULT, 0)
                    // 获取下载状态
                    val status = when (statusCode) {
                        0, 8, 9 -> Status.IDLE
                        1 -> Status.DOWNLOADING
                        2 -> Status.PAUSED
                        3 -> Status.SUCCESS
                        4 -> Status.FAILED
                        5 -> Status.INSTALLING
                        6 -> Status.INSTALLED
                        else -> Status.UNKNOWN
                    }
                    getRemoteProgress(packageName) { progress ->
                        callback(DownloadState(packageName, progress, status))
                    }
                } else {
                    callback(null)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "查询异常", e)
            callback(null)
        }
    }

    // 在 SDK 层提供 suspend 函数
/*    suspend fun queryDownloadStatus(packageName: String): DownloadState? =
        suspendCancellableCoroutine { continuation ->
            downloadManager?.getDownloadStatus(packageName) { result ->
                if (NeoDownloadManager.isSuccess(result)) {
                    val statusCode = result.getInt(NeoDownloadManager.RESULT, 0)
                    // 获取下载状态
                    val status = when (statusCode) {
                        0, 8, 9 -> Status.IDLE
                        1 -> Status.DOWNLOADING
                        2 -> Status.PAUSED
                        3 -> Status.SUCCESS
                        4 -> Status.FAILED
                        5 -> Status.INSTALLING
                        6 -> Status.INSTALLED
                        else -> Status.UNKNOWN
                    }

                    *//*getRemoteProgress(packageName) { progress ->
                        callback(DownloadState(packageName, progress, status))
                    }*//*
                    continuation.resume(DownloadState(packageName, progress, status))
                } else {
                    continuation.resume(null)
                }
            }
        }*/



    override fun registerCallback(packageName: String, progress: Int, callback: DownloadCallback) {
        val task = DownloadTask(packageName)
        //设置下载进度
        task.progress = progress
        downloadManager?.registerCallback(
            task,
            getDownloadListener(packageName, callback)
        )
    }

    override fun isDownloadEnabled(): Boolean {
        if (ctaStatus == null) {
            checkCTAStatus {
                ctaStatus = it
            }
        }
        return isSupportDownload && ctaStatus == true
    }


    private fun getDownloadListener(
        packageName: String,
        callback: DownloadCallback
    ): DownloadListener {
        return object : DownloadListener {
            override fun onStatusChanged(pkg: String?, status: DownloadStatus?) {
                if (pkg == packageName && status != null) {
                    Log.d(TAG, "download onStatusChanged: $pkg $status")
                    val newStatus = when (status) {
                        DownloadStatus.NOT_STARTED -> Status.IDLE
                        DownloadStatus.DOWNLOADING -> Status.DOWNLOADING
                        DownloadStatus.PAUSED -> Status.PAUSED
                        DownloadStatus.SUCCESS -> Status.SUCCESS
                        DownloadStatus.FAILED -> Status.FAILED
                        DownloadStatus.INSTALLING -> Status.INSTALLING
                        DownloadStatus.INSTALLED -> Status.INSTALLED
                        DownloadStatus.DELETED -> Status.IDLE
                        else -> Status.UNKNOWN
                    }
                    callback.onStatusChanged(newStatus)
                }
            }

            override fun onProgressChanged(pkg: String?, progress: Int) {
                if (pkg == packageName) {
                    Log.d(TAG, "download onProgressChanged: $pkg $progress")
                    callback.onProgress(progress)
                }
            }

            override fun onDisconnected() {
                super.onDisconnected()
                val status = getLocalStatus(packageName)
                if (status == DownloadStatus.DOWNLOADING || status == DownloadStatus.INSTALLED) {
                    callback.onStatusChanged(Status.FAILED)
                }
            }
        }
    }


    private fun checkCTAStatus(callback: (Boolean) -> Unit) {
        try {
            downloadManager?.isCTAAgreed { result ->
                if (NeoDownloadManager.isSuccess(result)) {
                    val isCTAEnabled = result.getBoolean(NeoDownloadManager.RESULT, false)
                    callback(isCTAEnabled)
                } else {
                    callback(true)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "检查CTA状态异常", e)
            callback(true)
        }
    }


    companion object {
        private const val TAG = "RealAppCenterSdk"
    }
}