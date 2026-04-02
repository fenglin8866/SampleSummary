package com.xxh.summary.download.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import cn.nubia.ns_lib.ILogger
import cn.nubia.ns_lib.LoggerManager
import cn.nubia.ns_lib.download.InitCallback
import cn.nubia.ns_lib.download.NeoDownloadManager
import cn.nubia.ns_lib.download.NeoDownloadManager.ConnectionExceptionHandler

object SDKManager {

    @SuppressLint("StaticFieldLeak")
    private var downloadManager: NeoDownloadManager? = null

    fun initSdk(context: Context) {
        downloadManager = NeoDownloadManager.getInstance()
        // 2. 初始化服务连接
        downloadManager?.init(context, object : InitCallback {
            override fun onSuccess() {
                Log.d("SDK", "初始化成功")
                //showToastOnUiThread("SDK 初始化成功")
            }

            override fun onFailure(result: cn.nubia.ns_lib.download.Result?) {
                Log.e("SDK", "初始化失败: ${result?.message}")
                //showToastOnUiThread("SDK 初始化失败: ${result?.message}")
            }
        })

        // 3. 设置异常处理器（推荐）
        // Set up exception handlers
        downloadManager?.setConnectionExceptionHandler(object :
            ConnectionExceptionHandler {
            override fun onServiceDisconnected() {
                Log.d("SDK", "服务连接断开，正在重新连接...")
                downloadManager?.connectToService(null)
            }

            override fun onStoreAppNotInstalled() {
                Log.d("SDK", "未安装应用中心")
            }

            override fun onStoreAppVersionNotSupported() {
                Log.d("SDK", "应用中心版本过低")
            }
        })

        downloadManager?.setCTAExceptionHandler {
            Log.e("SDK", "用户未同意隐私协议")
            try {
                downloadManager?.showCtaDialog()
            } catch (e: Exception) {
                Log.e("SDK", "CTAExceptionHandler error: ${e.message}")
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


}