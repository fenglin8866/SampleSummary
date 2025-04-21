package com.sample.core.demo.middleware


/**
 * 中间件接口：
 * 每个中间件可以拦截 Intent，做处理后返回新的 Intent
 */
interface IntentMiddleware<I : Any> {
    suspend fun intercept(intent: I): I
}