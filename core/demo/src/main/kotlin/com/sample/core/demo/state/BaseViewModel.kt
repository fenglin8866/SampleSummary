package com.sample.core.demo.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.demo.dispatcher.BaseIntentDispatcher
import com.sample.core.demo.event.UIEventDispatcher
import com.sample.core.demo.middleware.IntentMiddleware
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


abstract class BaseViewModel<S : Any, I : Any>(
    initialState: S
) : ViewModel() {

    // 1. StateFlow - UI状态流
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // 2. SharedFlow - UI事件流（Toast、Snackbar、导航等）
    private val _uiEvent = UIEventDispatcher()
    val uiEvent = _uiEvent.eventFlow

    // 3. Intent Dispatcher - Intent调度器
    private val dispatcher: BaseIntentDispatcher<I> by lazy {
        BaseIntentDispatcher(
            middlewares = buildMiddlewares(),
            onIntentHandled = { intent -> handleIntent(intent) }
        )
    }

    // 4. 单发一个 Intent
    fun dispatchIntent(intent: I, priority: Int = 0) {
        viewModelScope.launch {
            dispatcher.dispatch(intent, priority)
        }
    }

    // 5. 消费一串 Flow<Intent>（Redux 风格）
    fun consumeIntents(flow: Flow<I>) {
        viewModelScope.launch {
            flow.collectLatest { intent ->
                dispatchIntent(intent)
            }
        }
    }

    // 6. 发送 UI 事件
    protected fun sendEvent(event: Any) {
        viewModelScope.launch {
            _uiEvent.sendEvent(event)
        }
    }

    // 7. 更新 UI 状态
    protected fun setState(update: (S) -> S) {
        _uiState.update(update)
    }

    // --- 必须子类实现的方法 ---
    protected abstract fun handleIntent(intent: I)
    protected open fun buildMiddlewares(): List<IntentMiddleware<I>> = emptyList()
}