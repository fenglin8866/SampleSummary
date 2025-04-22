package com.sample.core.basic.ui.middleware


class PermissionMiddleware<I>(
    private val check: suspend (I) -> Boolean,
    private val onDenied: suspend (I) -> Unit
) : IntentMiddleware<I> {
    override suspend fun process(intent: I, next: suspend (I) -> Unit) {
        if (check(intent)) {
            next(intent)
        } else {
            onDenied(intent)
        }
    }
}