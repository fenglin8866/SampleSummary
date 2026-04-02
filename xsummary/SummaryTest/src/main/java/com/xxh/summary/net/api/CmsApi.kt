package com.xxh.summary.net.api

import com.xxh.summary.download.data.model.AppRankListResponse
import com.xxh.summary.net.model.BaseResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CmsApi {
    @FormUrlEncoded
    @POST("appstore/getRankList.do")
    suspend fun getApps(
        @FieldMap body: Map<String, String>
    ): BaseResponse<AppRankListResponse>
}