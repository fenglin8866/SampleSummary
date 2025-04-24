package com.sample.core.basic.mvi

import com.sample.core.basic.ui.utils.LogUtils

class LoggingMiddleware<I> : IntentMiddleware<I> {

    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        LogUtils.d(message = "intent log -> $intent")
        next(intent)
    }

}