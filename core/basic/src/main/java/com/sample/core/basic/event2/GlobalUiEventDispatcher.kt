package com.sample.core.basic.event2

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 依赖注释基类ViewModel和Activity/Fragment，
 */
@Singleton
class GlobalUiEventDispatcher @Inject constructor() {
    private val _events = MutableSharedFlow<GlobalUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<GlobalUiEvent> = _events

    fun emit(event: GlobalUiEvent) {
        _events.tryEmit(event)
    }

    /**
     * 紧急事件，会阻塞
     */
    suspend fun criticalDispatch(event: GlobalUiEvent) {
        _events.emit(event)
    }


    fun nonCriticalDispatch(event: GlobalUiEvent) {
        _events.tryEmit(event)
    }

    fun launchToast(message: String) {
        emit(GlobalUiEvent.Toast(message))
    }
}