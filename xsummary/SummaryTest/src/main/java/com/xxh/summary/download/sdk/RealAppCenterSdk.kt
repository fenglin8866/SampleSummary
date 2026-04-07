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
import com.xxh.summary.download.model.DownloadState
import com.xxh.summary.download.model.Status

class RealAppCenterSdk : AppCenterSdk {
    private var downloadManager: NeoDownloadManager? = null
    override fun initSdk(context: Context,callback: ((Boolean) -> Unit)?) {
        downloadManager = NeoDownloadManager.getInstance()
        // 2. 初始化服务连接
        downloadManager?.init(context, object : InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "初始化成功")
                callback?.invoke(true)
                //showToastOnUiThread("SDK 初始化成功")
            }

            override fun onFailure(result: cn.nubia.ns_lib.download.Result?) {
                Log.e(TAG, "初始化失败: ${result?.message}")
                //showToastOnUiThread("SDK 初始化失败: ${result?.message}")
                callback?.invoke(false)
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
                    Log.i("xxh444", "download onStatusChanged: $pkg $status")
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
                    Log.i("xxh444", "download onProgressChanged: $pkg $progress")
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

    override fun pause(packageName: String, callback: (DownloadState) -> Unit) {
        try {
            val result = downloadManager?.pauseDownload(packageName)
            Log.d(TAG, "暂停下载: $packageName start result=$result")
            if (result != null && NeoDownloadManager.isSuccess(result)) {
                Log.d(TAG, "暂停下载: $packageName success result=$result")
                // showToast("已暂停: ${app.appName}")
                // val progress = getLocalProgress(app.packageName)
                // updateUI(app.packageName, DownloadStatus.PAUSED, progress)
                val downloadState =
                    DownloadState(packageName, getLocalProgress(packageName), Status.PAUSED)
                callback(downloadState)
            }
        } catch (e: Exception) {
            Log.e(TAG, "暂停异常", e)
            // callback(getLocalProgress(packageName), Status.FAILED)
//             showToast("暂停失败")
        }
    }

    override fun resume(packageName: String, callback: (DownloadState) -> Unit) {
        try {
            /**/
            val result = downloadManager?.resumeDownload(packageName)
            Log.d(TAG, "恢复下载: $packageName start result=$result")
            if (result != null && NeoDownloadManager.isSuccess(result)) {
                Log.d(TAG, "恢复下载: $packageName success result=$result")
                /*showToast("继续下载: ${app.appName}")
                updateUI(
                    app.packageName,
                    DownloadStatus.DOWNLOADING,
                    getLocalProgress(app.packageName)
                )*/
                val downloadState =
                    DownloadState(packageName, getLocalProgress(packageName), Status.DOWNLOADING)
                callback(downloadState)
            }
        } catch (e: Exception) {
            Log.e(TAG, "恢复异常", e)
            //showToast("恢复失败")
            // callback(getLocalProgress(packageName), Status.FAILED)
        }
    }

    override fun open(packageName: String) {

    }


    override fun queryDownloadStatus(packageName: String) {
        try {
            val result = downloadManager?.getDownloadStatus(packageName)
            if (result == null) {
                Log.e(
                    TAG,
                    "查询失败：返回结果为空 getDownloadStatus returned null for package: $packageName"
                )
                return
            }

            // 解析结果
            val statusCode = result.getInt(NeoDownloadManager.RESULT, 0)
            val code = result.getInt(NeoDownloadManager.CODE, -1)
            val message = result.getString(NeoDownloadManager.MESSAGE, "")

            // 判断调用是否成功
            val isCallSuccess = NeoDownloadManager.isSuccess(result)

            // 获取下载状态
            val status = when (statusCode) {
                0 -> "任务不存在"
                1 -> "下载中"
                2 -> "已暂停"
                3 -> "下载成功"
                4 -> "下载失败"
                5 -> "安装中"
                6 -> "安装完成"
                8 -> "未开始"
                9 -> "任务被删除"
                else -> "未知状态"
            }

            // 构建结果显示
            val resultBuilder = StringBuilder()
            resultBuilder.append("包名: $packageName\n\n")
            resultBuilder.append("调用状态: ${if (isCallSuccess) "成功" else "失败"}\n")
            resultBuilder.append("错误码: $code\n")
            if (message.isNotEmpty()) {
                resultBuilder.append("错误信息: $message\n")
            }
            resultBuilder.append("\n")
            resultBuilder.append("下载状态: $status\n")
            resultBuilder.append("状态码: $statusCode\n")
            Log.d(TAG, "Query download status for $packageName: $resultBuilder")

        } catch (e: Exception) {
            val errorMsg = "查询失败：发生异常 - ${e.message}"
            Log.e(TAG, "Exception when calling getDownloadStatus", e)
        }
    }

    private fun registerCallback(packageName: String) {
        downloadManager?.registerCallback(cn.nubia.ns_lib.download.DownloadTask(packageName),
            object :
                DownloadListener {
                override fun onStatusChanged(
                    p0: String?,
                    p1: DownloadStatus?
                ) {
                    Log.i("xxh444", "packageName: $packageName onStatusChanged: $p0 $p1")
                }

                override fun onProgressChanged(p0: String?, p1: Int) {
                    Log.i("xxh444", "packageName: $packageName onProgressChanged: $p0 $p1")
                }
            })
    }



    override fun getDownloadingList(): List<DownloadTask> {
        TODO("Not yet implemented")
    }


    companion object {
        private const val TAG = "RealAppCenterSdk"
    }
}