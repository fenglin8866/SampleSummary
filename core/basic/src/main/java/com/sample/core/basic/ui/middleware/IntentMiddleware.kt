package com.sample.core.basic.ui.middleware

interface IntentMiddleware<I> {
    suspend fun process(intent: I, next: suspend (I) -> Unit)
}