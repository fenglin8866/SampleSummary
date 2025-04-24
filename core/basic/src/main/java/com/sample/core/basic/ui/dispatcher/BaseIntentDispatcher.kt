package com.sample.core.basic.ui.dispatcher

import com.sample.core.basic.ui.middleware.IntentMiddleware
import java.util.PriorityQueue

/**
 * intent调度器封装
 * 1、intent的优先级分配处理
 * 2、中间件执行
 */
class BaseIntentDispatcher<I>(
    private val middlewares: List<IntentMiddleware<I>> = emptyList(),
    private val onIntentHandled: suspend (I) -> Unit
) {
    private val intentQueue = PriorityQueue<PrioritizedIntent<I>>(compareByDescending { it.priority })

    private var isProcessing = false

    suspend fun dispatch(intent: I, priority: Int = 0) {
        intentQueue.add(PrioritizedIntent(intent, priority))
        processNext()
    }

    private suspend fun processNext() {
        if (isProcessing) return
        isProcessing = true
        while (intentQueue.isNotEmpty()) {
            val current = intentQueue.poll()
            if (current != null) {
                handleThroughMiddleware(current.intent)
            }
        }
        isProcessing = false
    }

    private suspend fun handleThroughMiddleware(intent: I) {
        var index = 0
        suspend fun next(i: I) {
            if (index < middlewares.size) {
                val middleware = middlewares[index++]
                middleware.process(i, ::next)
            } else {
                onIntentHandled(i)
            }
        }
        next(intent)
    }

    fun clear() {
        intentQueue.clear()
    }

}
