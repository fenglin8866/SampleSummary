package com.sample.lib.network.core

import com.sample.lib.network.interceptor.DynamicBaseUrlInterceptor
import com.sample.lib.network.interceptor.HeaderInterceptor
import com.sample.lib.network.interceptor.RetryInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpFactory {

    fun create(
        extraInterceptors: List<Interceptor> = emptyList(),
        debug: Boolean = false
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(createHeaderInterceptor())
            .addInterceptor(DynamicBaseUrlInterceptor())
            .addInterceptor(RetryInterceptor())

            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        // 日志
        if (debug) {
            builder.addInterceptor(createLoggingInterceptor())
        }

        // 业务自定义拦截器:埋点 、 加密 、 灰度发布
        extraInterceptors.forEach {
            builder.addInterceptor(it)
        }

        return builder.build()

    }

    fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun createHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor {
            mapOf(
                "Content-Type" to "application/json"
            )
        }
    }
}