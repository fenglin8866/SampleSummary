package com.sample.core.basic.ui.middleware

import com.sample.core.basic.ui.utils.LogUtils

class ExceptionCatchingMiddleware<I>(
    private val crashReporter: CrashReporter
) : IntentMiddleware<I> {

    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        try {
            next(intent)
        } catch (e: Exception) {
            crashReporter.report(e, "Exception while handling Intent: $intent")
        }
    }
}

interface CrashReporter {
    fun report(throwable: Throwable, message: String? = null)
}


object LocalCrashReporter : CrashReporter {
    override fun report(throwable: Throwable, message: String?) {
        LogUtils.d(message = "[CrashReporter] $message\n${throwable.stackTraceToString()}")
    }
}


/*
ExceptionCatchingMiddleware 应该是第一个挂的，优先保护后面的中间件
fun buildMiddlewares(): List<IntentMiddleware<MainUIIntent>> = listOf(
    ExceptionCatchingMiddleware(crashReporter),
    LoggingMiddleware(),
    ThrottleMiddleware(800),
    PermissionMiddleware(
        check = { intent -> checkPermission(intent) },
        onDenied = { sendEvent(UIEvent.Toast("请授权")) }
    )
)*/
