package com.xxh.summary.download.data.repository

import com.sample.lib.network.repository.BaseRepository
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.NetworkUtil
import javax.inject.Inject


class AppRepository @Inject constructor(private val api: AppApi) : BaseRepository() {

    /* suspend fun loadApps(): List<AppInfo>? {
         return api.getApps(NetworkUtil.getCommonParams()).data
     }*/

    fun loadTopApps() = request {
        api.getTopApps(NetworkUtil.getCommonParams())
    }

    fun loadHotApps() = request {
        val params = mutableMapOf<String, String>()
        params["pageSize"] = "10"
        api.getHotApps(NetworkUtil.getCommonParams(params))
    }

    fun loadRecentApps(packageName: String? = null) = request {
        val params = mutableMapOf<String, String>()
        params["pkgName"] = packageName ?: ""
        api.getRecentApps(NetworkUtil.getCommonParams(params))
    }

}