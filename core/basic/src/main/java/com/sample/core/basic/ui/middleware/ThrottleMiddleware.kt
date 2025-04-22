package com.sample.core.basic.ui.middleware


class ThrottleMiddleware<I>(
    private val intervalMs: Long = 500L
) : IntentMiddleware<I> {
    private val lastHandledTime = mutableMapOf<String, Long>()
    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        val key = intent.toString()
        val now = System.currentTimeMillis()
        val lastTime = lastHandledTime[key] ?: 0L
        if (now - lastTime >= intervalMs) {
            lastHandledTime[key] = now
            next(intent)
        } else {
            println("[Intent] Throttled: $intent")
        }
    }
}