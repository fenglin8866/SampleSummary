package com.sample.core.basic2.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class BaseViewModel<State : Any>(initialState: State) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    val uiState: StateFlow<State> = _uiState.asStateFlow()

    protected fun updateState(reducer: State.() -> State) {
        // _uiState.value = _uiState.value.reducer()
        val newState = _uiState.value.reducer()
        if (newState != _uiState.value) {
            _uiState.update { newState }
        }
    }

    /**
     * 添加对状态更新的限制，避免不必要的更新
     */
    protected fun updateState(state: State) {
        if (state != _uiState.value) {
            _uiState.value = state
        }
    }
}
