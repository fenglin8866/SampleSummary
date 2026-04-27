package com.sample.lib.network.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * 网络管理器（多BaseUrl）
 * 每个BaseUrl构建一个Retrofit实例，支持动态切换BaseUrl
 *
 * 1.当多个Repository使用同一个API时，可以缓存API
 * 2.不同的hostType需要使用不同的okHttpClient时，创建不同的Retrofit实例
 */
object NetworkManagerMultiBaseUrl {

    // ===== 全局配置 =====

    private val retrofitCache = mutableMapOf<String, Retrofit>()

    //Service 缓存（避免重复创建）
    private val serviceCache = mutableMapOf<Class<*>, Any>()


    private val okHttpClient: OkHttpClient by lazy {
        OkHttpFactory.create()
    }

   /*
   为了使网络库更灵活，移除相关配置信息
   fun <T> createCache(hostType: String, service: Class<T>): T {
        return serviceCache.getOrPut(service) {
            create(hostType, service) as Any
        } as T
    }

    fun <T> createInterceptors(
        hostType: String,
        service: Class<T>,
        extraInterceptors: List<Interceptor> = emptyList()
    ): T {
        val baseUrl = HostManager.getBaseUrl(hostType)
        val retrofit = getOrCreateRetrofit(baseUrl, extraInterceptors)
        return retrofit.create(service)
    }

    //创建 API（推荐方式）
    fun <T> create(hostType: String, service: Class<T>): T {
        val baseUrl = HostManager.getBaseUrl(hostType)
        val retrofit = getOrCreateRetrofit(baseUrl)
        return retrofit.create(service)
    }

        //创建通用API
    fun <T> createForBaseUrl(baseUrl: String, service: Class<T>): T {
        val retrofit = getOrCreateRetrofit(baseUrl)
        return retrofit.create(service)
    }

    */

    fun <T> createCache(baseUrl: String, service: Class<T>): T {
        return serviceCache.getOrPut(service) {
            create(baseUrl, service) as Any
        } as T
    }

    fun <T> create(baseUrl: String, service: Class<T>): T {
        val retrofit = getOrCreateRetrofit(baseUrl)
        return retrofit.create(service)
    }

    fun <T> createInterceptors(
        baseUrl: String,
        service: Class<T>,
        extraInterceptors: List<Interceptor> = emptyList()
    ): T {
        val retrofit = getOrCreateRetrofit(baseUrl, extraInterceptors)
        return retrofit.create(service)
    }

    /** 环境切换时调用 */
    fun onEnvChanged() {
        retrofitCache.clear() // 清空retrofit缓存
        serviceCache.clear() //  清空service缓存
    }

    private fun getOrCreateRetrofit(
        baseUrl: String,
        extraInterceptors: List<Interceptor> = emptyList()
    ): Retrofit {
        return retrofitCache.getOrPut(baseUrl) {
            val okHttpClient = OkHttpFactory.create(extraInterceptors)
            RetrofitFactory.create(baseUrl, okHttpClient)
        }
    }

    private fun getOrCreateRetrofit(baseUrl: String): Retrofit {
        return retrofitCache.getOrPut(baseUrl) {
            RetrofitFactory.create(
                baseUrl,
                okHttpClient
            )
        }
    }

}



