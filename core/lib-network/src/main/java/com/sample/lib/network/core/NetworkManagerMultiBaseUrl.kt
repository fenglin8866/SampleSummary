package com.sample.lib.network.core

import com.sample.lib.network.env.HostManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * 网络管理器（多BaseUrl）
 * 每个BaseUrl构建一个Retrofit实例，支持动态切换BaseUrl
 */
object NetworkManagerMultiBaseUrl {

    // ===== 全局配置 =====

    private val retrofitCache = mutableMapOf<String, Retrofit>()
    private lateinit var okHttpClient: OkHttpClient

    // ===== 初始化 =====
    fun init(
        debug: Boolean = false,
        extraInterceptors: List<Interceptor> = emptyList()
    ) {
        okHttpClient = OkHttpFactory.create(extraInterceptors, debug)
    }

    /** 创建 API（推荐方式） */
    fun <T> create(hostType: String, service: Class<T>): T {
        val baseUrl = HostManager.getBaseUrl(hostType)
        val retrofit = getOrCreateRetrofit(baseUrl)
        return retrofit.create(service)
    }

    /** 创建通用API*/
    fun <T> createForBaseUrl(baseUrl: String, service: Class<T>): T {
        val retrofit = getOrCreateRetrofit(baseUrl)
        return retrofit.create(service)
    }

    /** 环境切换时调用 */
    fun onEnvChanged() {
        retrofitCache.clear() // ⭐关键：清空缓存
    }

    private fun getOrCreateRetrofit(baseUrl: String): Retrofit {
        return retrofitCache.getOrPut(baseUrl) {
            RetrofitFactory.create(baseUrl, okHttpClient)
        }
    }

}



