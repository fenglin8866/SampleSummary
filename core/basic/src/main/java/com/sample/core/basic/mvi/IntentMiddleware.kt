package com.sample.core.basic.mvi


interface IntentMiddleware<I> {
    suspend fun process(intent: I, next: suspend (I) -> Unit)
}