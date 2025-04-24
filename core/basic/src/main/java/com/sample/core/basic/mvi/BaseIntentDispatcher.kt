package com.sample.core.basic.mvi

import java.util.PriorityQueue

class BaseIntentDispatcher<I>(
    private val middlewares: List<IntentMiddleware<I>> = emptyList(),
    val onIntentHandled: suspend (I) -> Unit
) {
    private val intentQueue =
        PriorityQueue<PrioritizedIntent<I>>(compareByDescending { it.priority })

    private var isProcessing = false

    suspend fun dispatch(intent: I) {
        intentQueue.add(PrioritizedIntent(intent, 0))
        processNext()
    }

    suspend fun dispatch(intent: I, priority: Int) {
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
                val current = middlewares[index++]
                current.process(i, ::next)
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