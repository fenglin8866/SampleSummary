package com.sample.core.demo.dispatcher

import com.sample.core.demo.middleware.IntentMiddleware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BaseIntentDispatcher<I : Any>(
    private val middlewares: List<IntentMiddleware<I>> = emptyList(),
    private val onIntentHandled: suspend (I) -> Unit,
) {

    private val mutex = Mutex() // 确保事务串行处理
    private val channel = Channel<IntentWrapper<I>>(Channel.UNLIMITED)

    fun CoroutineScope.start() {
        launch {
            channel.consumeAsFlow()
                .collect { wrapper ->
                    mutex.withLock {
                        handle(wrapper.intent)
                    }
                }
        }
    }

    suspend fun dispatch(intent: I, priority: Int = 0) {
        channel.send(IntentWrapper(intent, priority))
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