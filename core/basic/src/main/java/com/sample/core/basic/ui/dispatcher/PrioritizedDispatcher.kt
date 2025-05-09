package com.sample.core.basic.ui.dispatcher

import java.util.PriorityQueue

/**
 * 定义Intent优先级容器
 */
data class PrioritizedIntent<I>(
    val intent: I,
    val priority: Int = 0
)


class PrioritizedDispatcher<I>(
    private val baseDispatcher: IntentDispatcher<I>
) {
    private val queue = PriorityQueue<PrioritizedIntent<I>>(compareByDescending { it.priority })
    private var isRunning = false

    suspend fun enqueue(intent: PrioritizedIntent<I>) {
        queue.add(intent)
        if (!isRunning) {
            isRunning = true
            while (queue.isNotEmpty()) {
                val current = queue.poll()
                if (current != null) {
                    baseDispatcher.dispatch(current.intent)
                }
            }
            isRunning = false
        }
    }
}