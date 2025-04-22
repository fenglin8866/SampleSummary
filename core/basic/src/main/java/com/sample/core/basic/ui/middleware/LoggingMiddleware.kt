package com.sample.core.basic.ui.middleware

import com.sample.core.basic.ui.utils.LogUtils

class LoggingMiddleware<I> : IntentMiddleware<I> {
    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        LogUtils.d(message = "[Intent Log] -> $intent")
        next(intent)
    }
}