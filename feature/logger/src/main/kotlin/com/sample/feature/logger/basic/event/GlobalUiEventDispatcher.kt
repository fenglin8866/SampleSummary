package com.sample.feature.logger.basic.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalUiEventDispatcher @Inject constructor(){
    private val _events = MutableSharedFlow<GlobalUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<GlobalUiEvent> = _events

    fun emit(event: GlobalUiEvent) {
        _events.tryEmit(event)
    }
}