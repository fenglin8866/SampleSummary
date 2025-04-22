package com.sample.core.basic.ui.middleware

class LoggingMiddleware<I> : IntentMiddleware<I> {
    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        println("[Intent Log] -> $intent")
        next(intent)
    }
}