package com.sample.lib.network.test

import com.sample.lib.network.model.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    /**
     * 对象自动转JSON
     */
    @POST("user/login")
    suspend fun getUser(
        @Body body: LoginRequest
    ): BaseResponse<User>

    /**
     * Map形式，灵活
     */
    @POST("user/login")
    suspend fun login(
        @Body body: Map<String, Any>
    ): BaseResponse<User>


    /**
     * val json = JSONObject().apply {
     *     put("username", "test")
     *     put("password", "123456")
     * }
     *
     * val requestBody = json.toString()
     *     .toRequestBody("application/json".toMediaType())
     */
    @POST("user/login")
    suspend fun login(
        @Body body: RequestBody
    ): BaseResponse<User>
    /**
     * 适合临时切
     * 单接口切换 BaseUrl
     */
    @POST("user/info")
    suspend fun getUser(
        @Header("Base-Url") baseUrl: String
    )

    /**
     * 单接口动态添加Header
     */
    @POST("user/login")
    suspend fun login(
        @Header("Authorization") token: String,
        @Body body: LoginRequest
    ): BaseResponse<User>

}