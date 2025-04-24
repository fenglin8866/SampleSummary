package com.sample.core.basic.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Intent : Any>(initializer: State) : ViewModel() {
    private var _uiState = MutableStateFlow(initializer)

    private val _uiEvent = MutableSharedFlow<UIEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val dispatcher = BaseIntentDispatcher(
        middlewares = buildMiddleware(),
        onIntentHandled = {
            onIntentHandle(it)
        }
    )

    val uiState: StateFlow<State> = _uiState.asStateFlow()

    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    fun updateState(state: State) {
        _uiState.value = state
    }

    fun sendEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    open fun buildMiddleware(): List<IntentMiddleware<Intent>> = emptyList()


    fun dispatchIntent(intent: Intent) {
        viewModelScope.launch {
            dispatcher.dispatch(intent)
        }
    }

    fun dispatchIntents(vararg intents: Intent) {
        intents.forEach {
            dispatchIntent(it)
        }
    }

    fun consumeIntent(intentFlow: Flow<Intent>) {
        viewModelScope.launch {
            intentFlow.collectLatest {
                dispatcher.dispatch(it)
            }
        }
    }


    abstract fun onIntentHandle(intent: Intent)

}