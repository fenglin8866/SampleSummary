package com.sample.core.demo.dispatcher

import com.sample.core.demo.middleware.IntentMiddleware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.PriorityQueue

class BaseIntentDispatcher<I : Any>(
    private val middlewares: List<IntentMiddleware<I>> = emptyList(),
    private val onIntentHandled: suspend (I) -> Unit,
) {

    private val mutex = Mutex()
    private val queue = PriorityQueue<IntentWrapper<I>>(compareByDescending { it.priority })
    private val channel = Channel<Unit>(Channel.UNLIMITED)

    fun CoroutineScope.start() {
        launch {
            channel.consumeAsFlow()
                .collect {
                    mutex.withLock {
                        val wrapper = queue.poll() ?: return@withLock
                        handle(wrapper.intent)
                    }
                }
        }
    }

    suspend fun dispatch(intent: I, priority: Int = 0) {
        mutex.withLock {
            queue.offer(IntentWrapper(intent, priority))
        }
        channel.send(Unit)
    }

    private suspend fun handle(intent: I) {
        // 1. Middlewares 预处理
        val finalIntent = middlewares.fold(intent) { acc, middleware ->
            middleware.intercept(acc)
        }

        // 2. 交给真正的 ViewModel 处理
        onIntentHandled(finalIntent)
    }

    private data class IntentWrapper<I>(
        val intent: I,
        val priority: Int
    )
}