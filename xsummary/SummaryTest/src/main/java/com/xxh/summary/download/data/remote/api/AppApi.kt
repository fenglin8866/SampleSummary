package com.xxh.summary.download.data.remote.api

import com.sample.lib.network.model.BaseResponse
import com.xxh.summary.download.model.AppListResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AppApi {
    @FormUrlEncoded
    @POST("appstore/getRankList.do")
    suspend fun getTopApps(
        @FieldMap body: Map<String, String>
    ): BaseResponse<AppListResponse>

    @FormUrlEncoded
    @POST("appstore/getRecmdListByOaid.do")
    suspend fun getHotApps(
        @FieldMap body: Map<String, String>
    ): BaseResponse<AppListResponse>


    @FormUrlEncoded
    @POST("appstore/getRecmdList.do")
    suspend fun getRecentApps(
        @FieldMap body: Map<String, String>
    ): BaseResponse<AppListResponse>

}