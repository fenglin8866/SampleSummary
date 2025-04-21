package com.sample.core.demo.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * 通用 UI 事件发射器：
 * - 用 Channel 保证只消费一次
 * - 用 Flow 方式暴露给外部收集
 */
class UIEventDispatcher {

    private val _eventChannel = Channel<Any>(Channel.UNLIMITED)
    val eventFlow = _eventChannel.receiveAsFlow()

    suspend fun sendEvent(event: Any) {
        _eventChannel.send(event)
    }
}