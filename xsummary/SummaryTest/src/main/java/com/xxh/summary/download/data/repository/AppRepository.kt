package com.xxh.summary.download.data.repository

import com.xxh.summary.net.api.CmsApi
import com.xxh.summary.net.NetworkUtil
import com.xxh.summary.net.repository.BaseRepository

class AppRepository(private val api: CmsApi) : BaseRepository() {

    /* suspend fun loadApps(): List<AppInfo>? {
         return api.getApps(NetworkUtil.getCommonParams()).data
     }*/

    fun loadApps() = request {
        api.getApps(NetworkUtil.getCommonParams())
    }

   /* fun startDownload(packageName: String) {
        DownloadRepository.start(packageName)
    }

    fun pause(appId: String) {
        DownloadRepository.pause(appId)
    }

    fun resume(appId: String) {
        DownloadRepository.resume(appId)
    }*/
}