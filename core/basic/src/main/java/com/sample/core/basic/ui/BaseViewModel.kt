package com.sample.core.basic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.event.UIEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Intent : Any>(initialState: State) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    val uiState: StateFlow<State> = _uiState.asStateFlow()

  //  private val _uiEvent = MutableSharedFlow<UIEvent>()

    private val _uiEvent = MutableSharedFlow<UIEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    protected fun updateState(reducer: State.() -> State) {
        // _uiState.value = _uiState.value.reducer()
        _uiState.update(reducer)
    }

    protected fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    protected fun updateState(state: State) {
        _uiState.value = state
    }

    /**
     * intent批量分发
     */
    fun dispatchIntents(vararg intents: Intent) {
        intents.forEach { handleIntent(it) }
    }

    // DSL 风格 Intent 内部再分发
    open fun dispatchIntent(intent: Intent) {
        handleIntent(intent)
    }

    // 处理 UiIntent，更新 UIState 或发送事件
    protected abstract fun handleIntent(intent: Intent)

}
