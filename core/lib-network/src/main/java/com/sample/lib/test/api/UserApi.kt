package com.sample.lib.test.api

import com.sample.lib.network.model.BaseResponse
import com.sample.lib.test.repository.LoginRequest
import com.sample.lib.test.repository.User
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("user/login")
    suspend fun getUser(): BaseResponse<User>

    @POST("user/logout")
    suspend fun logout(): BaseResponse<Unit>

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


    @POST("user/login")
    suspend fun search(
        @Query("key") key: String
    ): BaseResponse<List<User>>

    //单接口切换 BaseUrl
    @POST("xxx")
    suspend fun test(
        @Header("Base-Url") baseUrl: String,
        @Body body: Any
    )

    @POST("user/getList")
    suspend fun getList(): BaseResponse<User>


}