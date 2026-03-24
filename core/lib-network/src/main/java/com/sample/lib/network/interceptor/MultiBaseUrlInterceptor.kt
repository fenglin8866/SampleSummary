package com.sample.lib.network.interceptor

import com.sample.lib.network.env.HostManager
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 多 BaseUrl 切换拦截器
 *
 * @POST("user/info")
 * suspend fun getUser(
 *     @Header("Host-Type") hostType: String
 * )
 */
class MultiBaseUrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val hostType = request.header("Host-Type")
            ?: return chain.proceed(request)

        val newBaseUrl = HostManager.getBaseUrl(hostType).toHttpUrl()

        val newUrl = request.url.newBuilder()
            .scheme(newBaseUrl.scheme)
            .host(newBaseUrl.host)
            .port(newBaseUrl.port)
            .build()

        val newRequest = request.newBuilder()
            .removeHeader("Host-Type")
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}