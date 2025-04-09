package com.sample.core.basic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UIState : Any, UIIntent : Any>(initialState: UIState) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UIState> = _uiState

    private val _eventFlow = MutableSharedFlow<UIEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventFlow: SharedFlow<UIEvent> = _eventFlow

    protected fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected fun updateState(reducer: UIState.() -> UIState) {
        _uiState.value = _uiState.value.reducer()
    }

    // 处理 UiIntent，更新 UIState 或发送事件
    abstract fun handleIntent(intent: UIIntent)

}
