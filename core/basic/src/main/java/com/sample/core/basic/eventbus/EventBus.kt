package com.sample.core.basic.eventbus

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * 发送事件（任意位置 ViewModel、Repo、Helper）
 * viewModelScope.launch {
 *     EventBus.send("global", GlobalEvent.ToastEvent("这是一个全局 Toast"))
 * }
 *
 * 接收事件（Activity / Fragment）
 * lifecycleScope.launchWhenStarted {
 *     EventBus.getFlow<GlobalEvent>("global").collect { event ->
 *         when (event) {
 *             is GlobalEvent.ToastEvent -> {
 *                 Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT).show()
 *             }
 *             is GlobalEvent.NavigateEvent -> {
 *                 // 跳转逻辑
 *             }
 *         }
 *     }
 * }
 */
object EventBus {
    private val eventMap = mutableMapOf<String, MutableSharedFlow<Any>>()
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getFlow(key: String): SharedFlow<T> {
        return eventMap.getOrPut(key) {
            MutableSharedFlow(
                replay = 0,
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        } as SharedFlow<T>
    }
    @Suppress("UNCHECKED_CAST")
    suspend fun <T : Any> send(key: String, event: T) {
        val flow = eventMap.getOrPut(key) {
            MutableSharedFlow(
                replay = 0,
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        } as MutableSharedFlow<T>
        flow.emit(event)
    }

    fun clear(key: String) {
        eventMap.remove(key)
    }
    fun hasObservers(key: String): Boolean {
        return ((eventMap[key] as? MutableSharedFlow<*>)?.subscriptionCount?.value ?: 0) > 0
    }
}
