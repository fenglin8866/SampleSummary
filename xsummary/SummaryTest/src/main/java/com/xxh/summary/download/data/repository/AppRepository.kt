package com.xxh.summary.download.data.repository

import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.net.api.CmsApi
import com.xxh.summary.download.domain.DownloadManager
import com.xxh.summary.net.NetworkUtil
import com.xxh.summary.net.repository.BaseRepository

class AppRepository(private val api: CmsApi) : BaseRepository() {
    /* suspend fun loadApps(): List<AppInfo>? {
         return api.getApps(NetworkUtil.getCommonParams()).data
     }*/

    fun loadApps() = request {
        api.getApps(NetworkUtil.getCommonParams())
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