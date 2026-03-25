package com.sample.lib.network.auth

import com.sample.lib.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("user/login")
    suspend fun refreshToken(
        @Body body: Map<String, Any>
    ): BaseResponse<TokenInfo>

}