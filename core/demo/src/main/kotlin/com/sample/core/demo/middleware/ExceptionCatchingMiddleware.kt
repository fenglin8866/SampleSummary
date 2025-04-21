package com.sample.core.demo.middleware

import com.sample.core.demo.utils.CrashReporter


/**
 * 异常捕获中间件：
 * 统一 try-catch，异常上传到 CrashReporter
 */
class ExceptionCatchingMiddleware<I : Any> : IntentMiddleware<I> {
    override suspend fun intercept(intent: I): I {
        return try {
            intent
        } catch (e: Throwable) {
            CrashReporter.report(e)
            throw e // 也可以选择 swallow，不抛出
        }
    }
}