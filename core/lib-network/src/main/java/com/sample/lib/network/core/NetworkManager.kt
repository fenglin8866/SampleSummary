package com.sample.lib.network.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * class App : Application() {
 *
 *     override fun onCreate() {
 *         super.onCreate()
 *
 *         NetworkManager.init(
 *             baseUrl = "https://api.xxx.com/",
 *             debug = BuildConfig.DEBUG
 *         )
 *     }
 * }
 */
object NetworkManager {

    // ===== 全局配置 =====
    private var baseUrl: String = ""
    private var debug: Boolean = false

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit

    //Service 缓存（避免重复创建）
    private val serviceCache = mutableMapOf<Class<*>, Any>()

    // ===== 初始化 =====
    fun init(
        baseUrl: String,
        debug: Boolean = false,
        extraInterceptors: List<Interceptor> = emptyList()
    ) {
        this.baseUrl = baseUrl
        this.debug = debug
        okHttpClient = OkHttpFactory.create(extraInterceptors)
        retrofit = RetrofitFactory.create(baseUrl, okHttpClient)
    }

    // ===== 创建 Service =====
    fun <T> create(service: Class<T>): T {
        check(::retrofit.isInitialized) {
            "NetworkManager 未初始化，请先调用 init()"
        }
        return retrofit.create(service)
    }

    fun <T> createCache(service: Class<T>): T {
        return serviceCache.getOrPut(service) {
            retrofit.create(service) as Any
        } as T
    }


    // ===== 获取 Retrofit（特殊场景）=====
    fun getRetrofit(): Retrofit = retrofit

    fun getOkHttpClient(): OkHttpClient = okHttpClient

    // ===== 动态切换 BaseUrl（全局）=====
    fun switchBaseUrl(newBaseUrl: String) {
        baseUrl = newBaseUrl
        retrofit = RetrofitFactory.create(baseUrl, okHttpClient)
    }
}