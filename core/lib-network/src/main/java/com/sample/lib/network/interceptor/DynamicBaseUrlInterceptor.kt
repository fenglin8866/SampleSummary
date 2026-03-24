package com.sample.lib.network.interceptor

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response


/**
 * 多 BaseUrl（动态域名切换）
 *
 * @POST("xxx")
 * suspend fun test(
 *     @Header("Base-Url") baseUrl: String,
 *     @Body body: Any
 * )
 *
 */
class DynamicBaseUrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val baseUrl = request.header("Base-Url")
            ?: return chain.proceed(request)

        val newBase = baseUrl.toHttpUrl()

        val newUrl = request.url.newBuilder()
            .scheme(newBase.scheme)
            .host(newBase.host)
            .port(newBase.port)
            .build()

        val newRequest = request.newBuilder()
            .removeHeader("Base-Url")
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}