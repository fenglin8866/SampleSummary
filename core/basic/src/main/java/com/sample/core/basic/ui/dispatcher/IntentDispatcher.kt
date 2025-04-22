package com.sample.core.basic.ui.dispatcher

import com.sample.core.basic.ui.middleware.IntentMiddleware

class IntentDispatcher<I>(
    private val middlewares: List<IntentMiddleware<I>> = emptyList(),
    private val onIntentHandled: suspend (I) -> Unit
) {
    suspend fun dispatch(intent: I) {
        var index = 0
        suspend fun next(i: I) {
            if (index < middlewares.size) {
                val current = middlewares[index++]
                current.process(i, ::next)
            } else {
                onIntentHandled(i) // 最终由 ViewModel 处理
            }
        }
        next(intent)
    }
}