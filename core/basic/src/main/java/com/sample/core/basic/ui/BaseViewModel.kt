package com.sample.core.basic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.event.UIEvent
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

    protected fun updateState(state: UIState) {
        _uiState.value = state
    }

    /**
     * intent批量分发
     */
    fun dispatchIntents(vararg intents: UIIntent) {
        intents.forEach { handleIntent(it) }
    }

    // DSL 风格 Intent 内部再分发
    protected fun dispatch(intent: UIIntent) {
        handleIntent(intent)
    }

    // 处理 UiIntent，更新 UIState 或发送事件
    abstract fun handleIntent(intent: UIIntent)

}
