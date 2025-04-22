package com.sample.core.basic.ui

import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.dispatcher.BaseIntentDispatcher
import com.sample.core.basic.ui.middleware.IntentMiddleware
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseMiddlewareViewModel<State : Any, Intent : Any>(
    initialState: State
) : BaseViewModel<State,Intent>(initialState) {

    private val dispatcher = BaseIntentDispatcher(
        middlewares = this.buildMiddlewares(),
        onIntentHandled = { handleIntent(it) }
    )

    protected open fun buildMiddlewares(): List<IntentMiddleware<Intent>> = emptyList()


    //普通 dispatch 单发一个 Intent
    override fun dispatchIntent(intent: Intent) {
        dispatchIntent(intent,0)
    }

    //普通 dispatch 单发一个 Intent
    fun dispatchIntent(intent: Intent, priority: Int) {
        viewModelScope.launch {
            dispatcher.dispatch(intent, priority)
        }
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

}
