package com.sample.core.basic2.viewmodel

abstract class BaseIntentViewModel<State : Any, Intent : Any>(initialState: State) :
    BaseViewModel<State>(initialState) {

    /**
     * intent批量分发，带错误处理
     */
    fun dispatchIntents(vararg intents: Intent) {
        intents.forEach { intent ->
            try {
                handleIntent(intent)
            } catch (e: Exception) {
                // 可以在这里添加日志记录或其他错误处理逻辑
                handleIntentError(e, intent)
            }
        }
    }

    // DSL 风格 Intent 内部再分发，带错误处理
    open fun dispatchIntent(intent: Intent) {
        try {
            handleIntent(intent)
        } catch (e: Exception) {
            handleIntentError(e, intent)
        }
    }

    /**
     * 处理 Intent 时发生的错误
     */
    protected open fun handleIntentError(error: Exception, intent: Intent) {
        // 默认不执行任何操作，子类可以重写此方法来处理错误
    }

    // 处理 UiIntent，更新 UIState 或发送事件
    protected abstract fun handleIntent(intent: Intent)
}
