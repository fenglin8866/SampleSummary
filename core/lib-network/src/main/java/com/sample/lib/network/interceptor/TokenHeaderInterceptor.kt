package com.sample.lib.network.interceptor

import com.sample.lib.network.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder().apply {
            addHeader("Content-Type", "application/json")

            TokenManager.token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}