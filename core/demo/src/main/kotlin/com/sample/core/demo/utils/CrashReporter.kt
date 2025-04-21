package com.sample.core.demo.utils

/**
 * 统一异常打点工具（可以接入 Crashlytics / Bugly 等）
 */
object CrashReporter {
    fun report(e: Throwable) {
        // 打点到你的异常收集平台
        println("CrashReporter: ${e.localizedMessage}")
    }
}