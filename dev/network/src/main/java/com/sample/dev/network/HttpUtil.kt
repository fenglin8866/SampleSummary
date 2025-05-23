package com.sample.dev.network

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpUtil {
    fun requestUrl() {
        val okHttpClient = OkHttpClient.Builder()
            .dispatcher(Dispatcher())//用于设置策略和执行异步请求的调度程序。不能为 null。
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val url = request.url.toUrl()
                val response = chain.proceed(request)
                val responseBody = response.body
                response
            })
            .build()
        val request = Request.Builder().url("").get().build()

        val call = okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
            }

        })
    }
}