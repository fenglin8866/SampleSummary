package com.sample.feature.upgrade

import android.content.Context
import cn.nubia.upgrade.api.NubiaUpdateConfiguration
import cn.nubia.upgrade.api.NubiaUpgradeManager
import cn.nubia.upgrade.api.RunMode
import cn.nubia.upgrade.http.IDownLoadListener
import cn.nubia.upgrade.http.IGetVersionListener
import cn.nubia.upgrade.model.VersionData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUpgradeManager @Inject constructor(@ApplicationContext val context: Context) {

    private var isDebugEnv = false

    private val mManager =
        NubiaUpgradeManager.getInstance(context, getAppKey(), getSecretKey())

    private var data: VersionData? = null

    init {
        val build = NubiaUpdateConfiguration.Builder()
        val configuration = build
            .setAbroadApp(false)//app是国内应用还是海外应用，不设置，默认是国内
            .setReportEnabled(true)//升级是否进行数据上报，不设置，默认是上报
            //.setAppID("xxxx") //设置应用id，用于个性化升级，不设置，默认优先VAID，取不到本地生成随机数存储
            .setDownloadRunMode(
                RunMode.ForegroundRunMode(
                    R.drawable.ic_launcher_foreground,
                    "下载"
                )
            )
            .setInstallRunMode(RunMode.ForegroundRunMode(R.drawable.ic_launcher_foreground, "安装"))
            .build()
        mManager.setConfiguration(configuration)
        mManager.debug(isDebugEnv)
        //NuLog.debug(true)
    }

    fun checkUpdate(callback: (VersionData?, Int?) -> Unit) {
        mManager.getVersion(context, object : IGetVersionListener {
            override fun onGetNewVersion(versionData: VersionData) {
                data = versionData
                callback(versionData, null)
            }

            override fun onGetNoVersion() {
                callback(null, null)
            }

            override fun onError(error: Int) {
                callback(null, error)
            }
        })
    }

    fun downloadApp(callback: (Int?, String?) -> Unit) {
        if (data != null) {
            mManager.addDownLoadListener(object : IDownLoadListener {
                override fun onStartDownload() {
                }

                override fun onDownloadProgress(progress: Int) {
                    callback(progress, null)
                }

                override fun onDownloadPause() {
                }

                override fun onResumeDownload() {
                }

                override fun onDownloadComplete(path: String?) {
                    callback(null, path)
                }

                override fun onDownloadError(errorCode: Int) {
                }
            })
            mManager.startDownload(context, data)
        }
    }

    fun downloadPause() {
        mManager.pauseDownload()
    }

    fun installApp() {
        if (isApkExist()) {
            mManager.install(context, data)
        }
    }

    fun isApkExist(): Boolean {
        return mManager.isApkExist(data)
    }

    /**
     * 用于个性化升级
     */
    fun getAppId(): String {
        return mManager.appId
    }

    fun getAppVersionInfo(): String {
        return mManager.appVersionInfo
    }

    private fun getAppKey() = if (isDebugEnv) DEBUG_APP_KEY else RELEASE_APP_KEY

    private fun getSecretKey() = if (isDebugEnv) DEBUG_SECRET_KEY else RELEASE_SECRET_KEY

    companion object {
        const val DEBUG_APP_KEY = "OyMhJ7J0879dd2ed"
        const val DEBUG_SECRET_KEY = "8df08ed0dbe9fb5b"
        const val RELEASE_APP_KEY = "OJBi01458bd77b4b"
        const val RELEASE_SECRET_KEY = "0400cced4c5fc0fd"
    }
}