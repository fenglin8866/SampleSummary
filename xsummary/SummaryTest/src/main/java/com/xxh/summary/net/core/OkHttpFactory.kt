package com.xxh.summary.net.core

import com.xxh.summary.net.env.EnvManager
import com.xxh.summary.net.interceptor.HeaderInterceptor
import com.xxh.summary.net.interceptor.RetryInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpFactory {

    fun create(
        extraInterceptors: List<Interceptor> = emptyList()
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            //.addInterceptor(createHeaderInterceptor())
            .addInterceptor(RetryInterceptor())

            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        // 日志
//        if (EnvManager.isTestEnv()) {
        if (true) {
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
                //"Content-Type" to "application/json"
            )
        }
    }
}