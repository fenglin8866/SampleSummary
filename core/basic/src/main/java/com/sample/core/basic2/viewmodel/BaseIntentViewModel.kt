package com.sample.core.basic2.viewmodel

abstract class BaseIntentViewModel<State : Any, Intent : Any>(initialState: State) :
    BaseViewModel<State>(initialState) {

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
