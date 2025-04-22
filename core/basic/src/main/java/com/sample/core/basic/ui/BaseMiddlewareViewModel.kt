package com.sample.core.basic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.dispatcher.BaseIntentDispatcher
import com.sample.core.basic.ui.middleware.IntentMiddleware
import com.sample.core.basic.ui.event.UIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMiddlewareViewModel<State : Any, Intent : Any>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()

    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    protected fun updateState(reducer: State.() -> State) {
        _uiState.update(reducer)
    }

    protected fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private val dispatcher = BaseIntentDispatcher(
        middlewares = buildMiddlewares(),
        onIntentHandled = { handleIntent(it) }
    )

    protected open fun buildMiddlewares(): List<IntentMiddleware<Intent>> = emptyList()


    //普通 dispatch 单发一个 Intent
    fun dispatchIntent(intent: Intent, priority: Int = 0) {
        viewModelScope.launch {
            dispatcher.dispatch(intent, priority)
        }
    }


    /**
     * intent批量分发
     */
    fun dispatchIntents(vararg intents: Intent) {
        intents.forEach { handleIntent(it) }
    }


    /**
     * Flow 消费一串 Intent
     * 核心思路：
     * 原本 dispatchIntent(intent) 是主动调用。
     * 现在让 Intent 可以来自一个 Flow，不断监听、消费 Intent！
     *
     * collectLatest：只处理最新的 Intent，之前的如果没处理完直接取消（防止堆积）
     * 自动转发给内部 dispatchIntent(intent)
     */
    fun consumeIntents(flow: Flow<Intent>) {
        viewModelScope.launch {
            flow.collectLatest { intent ->
                dispatchIntent(intent)
            }
        }
    }

    protected abstract fun handleIntent(intent: Intent)

}
