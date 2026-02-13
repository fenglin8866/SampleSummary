package com.sample.core.basic2.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * 带UI事件ViewModel
 * 侧重功能UI事件的处理
 */
abstract class BaseIntentUIEventViewModel<State : Any, Intent : Any, Event : Any>(initialState: State) :
    BaseIntentViewModel<State, Intent>(initialState) {

    private val _uiEvent = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    val uiEvent: SharedFlow<Event> = _uiEvent.asSharedFlow()

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

}
