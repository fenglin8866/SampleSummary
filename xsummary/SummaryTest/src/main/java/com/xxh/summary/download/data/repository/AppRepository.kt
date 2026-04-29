package com.xxh.summary.download.data.repository

import com.sample.lib.network.model.BaseResponse
import com.sample.lib.network.model.ResultState
import com.sample.lib.network.repository.BaseRepository
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.NetworkUtil
import com.xxh.summary.download.model.AppListResponse
import javax.inject.Inject


class AppRepository @Inject constructor(private val api: AppApi) : BaseRepository() {

    /* suspend fun loadApps(): List<AppInfo>? {
         return api.getApps(NetworkUtil.getCommonParams()).data
     }*/
    private var cache: BaseResponse<AppListResponse>? = null

    fun loadTopApps() = request {
        if (cache != null) {
            cache!!
        } else {
            val response = api.getTopApps(NetworkUtil.getCommonParams())
            if (response.code == 0 && response.data?.dataList != null) {
                cache = response
            }
            response
        }
    }

/*    fun loadTopApps() = request {
        api.getTopApps(NetworkUtil.getCommonParams())
    }*/

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