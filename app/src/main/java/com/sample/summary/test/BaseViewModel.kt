package com.sample.summary.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.sample.summary.test.UIIntent.OnLoginClicked
import com.sample.summary.test.UIIntent.OnNavigateToHome

abstract class BaseViewModel<UIState : Any, UIIntent : Any>(initialState: UIState) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UIState> = _uiState

    private val _eventFlow = MutableSharedFlow<UIEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventFlow: SharedFlow<UIEvent> = _eventFlow

    // 处理 UiIntent，更新 UIState 或发送事件
    fun handleIntent(intent: UIIntent) {
        when (intent) {
            is OnLoginClicked -> onLoginClicked()
            is OnNavigateToHome -> onNavigateToHome()
        }
    }

    protected fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected fun updateState(reducer: UIState.() -> UIState) {
        _uiState.value = _uiState.value.reducer()
    }

    // UIIntent 实现
    abstract fun onLoginClicked()
    abstract fun onNavigateToHome()
}
