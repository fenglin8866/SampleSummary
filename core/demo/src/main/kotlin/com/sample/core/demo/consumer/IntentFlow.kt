package com.sample.core.demo.consumer


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 挂载 Intent 流消费者
 */
fun <I : Any> Flow<I>.consumeIn(
    scope: CoroutineScope,
    consumer: suspend (I) -> Unit
) {
    scope.launch {
        this@consumeIn.collectLatest { intent ->
            consumer(intent)
        }
    }
}