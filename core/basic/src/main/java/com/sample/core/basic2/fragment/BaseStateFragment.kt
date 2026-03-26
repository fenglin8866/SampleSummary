package com.sample.core.basic2.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 具有状态处理能力的基础 Fragment
 * @param VB ViewBinding 类型
 * @param S 状态类型
 */
abstract class BaseStateFragment<VB : ViewBinding, State : Any> : BaseFragment<VB>() {

    override fun setup() {
        super.setup()
        collectUiStates()
    }

    protected fun collectUiStates() {
        provideUIState()?.let {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    it.collect { state ->
                        render(state)
                    }
                }
            }
        }
    }

    abstract fun provideUIState(): StateFlow<State>?

    /**
     * 根据状态进行UI渲染
     */
    abstract fun render(state: State)

}