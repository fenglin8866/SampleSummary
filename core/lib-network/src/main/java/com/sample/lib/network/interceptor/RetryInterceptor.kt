package com.sample.lib.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Retry 拦截器（失败重试）
 */
class RetryInterceptor(
    private val maxRetry: Int = 2
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var tryCount = 0
        var lastException: Exception? = null

        while (tryCount <= maxRetry) {
            try {
                return chain.proceed(chain.request())
            } catch (e: Exception) {
                lastException = e
                tryCount++
            }
        }

        throw lastException ?: RuntimeException("Unknown network error")
    }
}