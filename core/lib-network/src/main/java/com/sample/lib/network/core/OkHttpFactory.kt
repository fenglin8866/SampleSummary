package com.sample.lib.network.core

import com.sample.lib.network.interceptor.HeaderInterceptor
import com.sample.lib.network.interceptor.RetryInterceptor
import com.sample.lib.network.util.NetLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpFactory {

    fun create(
        extraInterceptors: List<Interceptor> = emptyList(),
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            //.addInterceptor(createHeaderInterceptor())
            //.addInterceptor(DynamicBaseUrlInterceptor())
            .addInterceptor(RetryInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        // 日志
        if (NetLog.isEnabled) {
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
                "X-Requested-With" to "XML HttpRequest",
                "User-Agent" to "Mozilla\\/5.0 (Linux; Android build_version; build_model Build\\/build_id) AppleWebKit\\/537.36(KHTML,like Gecko)Version\\/4.0 Chrome\\/42.0.2311.154 Mobile Safari\\/537.36\""
            )
        }
    }
}