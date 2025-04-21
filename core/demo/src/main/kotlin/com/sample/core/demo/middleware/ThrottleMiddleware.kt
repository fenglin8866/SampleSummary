package com.sample.core.demo.middleware

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * 防抖中间件：
 * 500ms 内只放行一次 Intent
 */
class ThrottleMiddleware<I : Any>(
    private val windowDuration: Duration = 500.milliseconds
) : IntentMiddleware<I> {

    private var lastActionTime = 0L
    private val mutex = Mutex()

    override suspend fun intercept(intent: I): I {
        mutex.withLock {
            val now = System.currentTimeMillis()
            if (now - lastActionTime >= windowDuration.inWholeMilliseconds) {
                lastActionTime = now
                return intent
            } else {
                throw IgnoreIntentException()
            }
        }
    }

    class IgnoreIntentException : Throwable()
}