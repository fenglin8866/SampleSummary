package com.sample.lib.network.test

import com.sample.lib.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Body body: LoginRequest
    ): BaseResponse<User>


    @POST("user/login")
    suspend fun login(
        @Header("Authorization") token: String,
        @Body body: LoginRequest
    ): BaseResponse<User>

    //单接口切换 BaseUrl
    @POST("xxx")
    suspend fun test(
        @Header("Base-Url") baseUrl: String,
        @Body body: Any
    )
}