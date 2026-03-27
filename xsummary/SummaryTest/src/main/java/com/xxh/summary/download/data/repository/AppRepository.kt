package com.xxh.summary.download.data.repository

import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.domain.DownloadManager

class AppRepository(
    private val api: AppApi
) {

    suspend fun loadApps(): List<AppInfo> {
        return api.getApps()
    }

    fun startDownload(app: AppInfo) {
        DownloadManager.start(app)
    }

    fun pause(appId: String) {
        DownloadManager.pause(appId)
    }

    fun resume(appId: String) {
        DownloadManager.resume(appId)
    }
}