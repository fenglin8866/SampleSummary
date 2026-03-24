package com.sample.lib.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/*class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder().apply {

            addHeader("Content-Type", "application/json")

          *//*  TokenManager.token?.let {
                addHeader("Authorization", "Bearer $it")
            }*//*

        }.build()

        return chain.proceed(newRequest)
    }
}*/
/**
 * HeaderInterceptor {
 *     mapOf(
 *         "Content-Type" to "application/json",
 *         "Authorization" to "Bearer ${TokenManager.token}"
 *     )
 * }
 */
class HeaderInterceptor(
    private val headerProvider: () -> Map<String, String>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val builder = request.newBuilder()

        // 添加全局 Header
        headerProvider().forEach { (key, value) ->
            builder.addHeader(key, value)
        }

        return chain.proceed(builder.build())
    }
}