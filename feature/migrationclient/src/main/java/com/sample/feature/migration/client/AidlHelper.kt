package com.sample.feature.migration.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper

/**
 * 迁移完成后通知remote应用
 * 1、标记状态，表示已经迁移过，之后的迁移请求不再响应
 * 2、删除迁移成功后的资源
 * 3、退出应用
 */
class AidlHelper {

    private var serviceConnection: ServiceConnection? = null
    private var isBound = false

    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null

    /**
     * 保证目标 Provider 的进程存活
     * @param context Context
     * @param targetPackage 目标包名，比如 com.nubia.nubrowser
     * @param targetService 目标Service全名，比如 com.nubia.nubrowser.MyProviderKeepAliveService
     * @param timeoutMs 超时时间，默认 10秒
     * @param onSuccess bind成功后回调
     * @param onTimeout 超时回调（可选）
     */
    fun ensureProviderAlive(
        context: Context,
        targetPackage: String,
        targetService: String,
        timeoutMs: Long = 10_000,
        onSuccess: (() -> Unit)? = null,
        onTimeout: (() -> Unit)? = null
    ) {
        if (isBound) {
            onSuccess?.invoke()
            return
        }

        val intent = Intent()
        intent.component = ComponentName(targetPackage, targetService)

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                try {
                    val remoteService = IMigrationAidlInterface.Stub.asInterface(service)
                    isBound = true
                    timeoutRunnable?.let { handler.removeCallbacks(it) }
                    onSuccess?.invoke()
                    remoteService.onFinish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isBound = false
            }
        }

        val bound = context.bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)

        if (!bound) {
            try {
                serviceConnection?.let {
                    context.unbindService(it)
                }
                // bindService直接失败
                serviceConnection = null

            } catch (e: Exception) {
                e.printStackTrace()
            }
            onTimeout?.invoke()
            return
        }

        // 超时保护
        timeoutRunnable = Runnable {
            if (!isBound) {
                unbind(context)
                onTimeout?.invoke()
            }
        }
        handler.postDelayed(timeoutRunnable!!, timeoutMs)
    }

    /**
     * 主动解绑
     */
    fun unbind(context: Context) {
        if (isBound && serviceConnection != null) {
            try {
                context.unbindService(serviceConnection!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        serviceConnection = null
        isBound = false
        timeoutRunnable?.let { handler.removeCallbacks(it) }
    }
}