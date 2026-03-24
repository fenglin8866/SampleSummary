package com.sample.lib.network.test

import com.sample.lib.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("user/login")
    suspend fun getUser(
        @Body body: LoginRequest
    ): BaseResponse<User>


    /**
     * 适合临时切
     * 单接口切换 BaseUrl
     */
    @POST("user/info")
    suspend fun getUser(
        @Header("Base-Url") baseUrl: String
    )
}