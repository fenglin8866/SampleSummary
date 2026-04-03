package com.xxh.summary.download.sdk

import android.content.Context
import android.util.Log
import cn.nubia.ns_lib.ILogger
import cn.nubia.ns_lib.LoggerManager
import cn.nubia.ns_lib.download.DownloadListener
import cn.nubia.ns_lib.download.DownloadRequest
import cn.nubia.ns_lib.download.DownloadStatus
import cn.nubia.ns_lib.download.InitCallback
import cn.nubia.ns_lib.download.NeoDownloadManager
import cn.nubia.ns_lib.download.NeoDownloadManager.ConnectionExceptionHandler
import com.xxh.summary.download.model.Status

class RealAppCenterSdk : AppCenterSdk {
    private var downloadManager: NeoDownloadManager? = null
    override fun initSdk(context: Context) {
        downloadManager = NeoDownloadManager.getInstance()
        // 2. 初始化服务连接
        downloadManager?.init(context, object : InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "初始化成功")
                //showToastOnUiThread("SDK 初始化成功")
            }

            override fun onFailure(result: cn.nubia.ns_lib.download.Result?) {
                Log.e(TAG, "初始化失败: ${result?.message}")
                //showToastOnUiThread("SDK 初始化失败: ${result?.message}")
            }
        })

        // 3. 设置异常处理器（推荐）
        // Set up exception handlers
        downloadManager?.setConnectionExceptionHandler(object :
            ConnectionExceptionHandler {
            override fun onServiceDisconnected() {
                Log.d(TAG, "服务连接断开，正在重新连接...")
                downloadManager?.connectToService(null)
            }

            override fun onStoreAppNotInstalled() {
                Log.d(TAG, "未安装应用中心")
            }

            override fun onStoreAppVersionNotSupported() {
                Log.d(TAG, "应用中心版本过低")
            }
        })

        downloadManager?.setCTAExceptionHandler {
            Log.e(TAG, "用户未同意隐私协议")
            try {
                downloadManager?.showCtaDialog()
            } catch (e: Exception) {
                Log.e(TAG, "CTAExceptionHandler error: ${e.message}")
            }
        }

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
        request.isAllowMobileNetwork = false

        val listener = object : DownloadListener {
            override fun onStatusChanged(pkg: String?, status: DownloadStatus?) {
                if (pkg == packageName && status != null) {
                    Log.d(
                        "xxh123xx",
                        "开始下载: $packageName downloadStatus=$status progress=${
                            getLocalProgress(packageName)
                        }"
                    )
                    val newStatus = when (status) {
                        DownloadStatus.NOT_STARTED -> Status.IDLE
                        DownloadStatus.DOWNLOADING -> Status.DOWNLOADING
                        DownloadStatus.PAUSED -> Status.PAUSED
                        DownloadStatus.SUCCESS -> Status.SUCCESS
                        DownloadStatus.FAILED -> Status.FAILED
                        DownloadStatus.INSTALLING -> Status.INSTALLING
                        DownloadStatus.INSTALLED -> Status.INSTALLED
                        else -> Status.UNKNOWN
                    }
                    callback.onStatusChanged(newStatus)
                }
            }

            override fun onProgressChanged(pkg: String?, progress: Int) {
                if (pkg == packageName) {
                    Log.d(
                        "xxh123",
                        "开始下载: $packageName downloadStatus=${getLocalStatus(packageName)} progress=${
                            getLocalProgress(packageName)
                        }"
                    )
                    callback.onProgress(progress)
                }
            }
        }

        try {
            val result = downloadManager?.startDownload(request, listener)
            if (result != null && NeoDownloadManager.isSuccess(result)) {
                Log.d(TAG, "开始下载: $packageName")
            } else {
                Log.d(TAG, "下载启动失败")
            }
        } catch (e: Exception) {
            Log.e(TAG, "下载异常", e)
        }
    }

    private fun getLocalProgress(packageName: String): Int {
        return downloadManager?.getLocalDownloadProgress(packageName) ?: 0
    }

    private fun getLocalStatus(packageName: String): DownloadStatus {
        val status = downloadManager?.getLocalDownloadStatus(packageName)
        return status ?: DownloadStatus.NOT_STARTED
    }

    override fun pause(packageName: String) {
        TODO("Not yet implemented")
    }

    override fun resume(packageName: String) {
        TODO("Not yet implemented")
    }

    override fun getDownloadingList(): List<DownloadTask> {
        TODO("Not yet implemented")
    }


    companion object {
        private const val TAG = "RealAppCenterSdk"
    }
}