package com.sample.lib.network.test

import com.sample.lib.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi2 {

    @POST("user/login")
    suspend fun getUser(): BaseResponse<User>

    @POST("user/login")
    suspend fun logout(): BaseResponse<Unit>

    @POST("user/login")
    suspend fun search(@Query("key") key: String): BaseResponse<List<User>>

    @POST("user/login")
    suspend fun login(
        @Body body: LoginRequest
    ): BaseResponse<User>
}