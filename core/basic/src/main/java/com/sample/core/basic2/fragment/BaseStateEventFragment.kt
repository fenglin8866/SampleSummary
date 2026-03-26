package com.sample.core.basic2.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/**
 * 具有状态和事件处理能力的基础 Fragment
 * @param VB ViewBinding 类型
 * @param S 状态类型
 * @param E 事件类型
 */
abstract class BaseStateEventFragment<VB : ViewBinding, State : Any, Event : Any> :
    BaseStateFragment<VB, State>() {

    override fun setup() {
        super.setup()
        collectUiEvents()
    }

    protected fun collectUiEvents() {
        provideUIEvents()?.let {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    it.collect { event ->
                        handleUIEvents(event)
                    }
                }
            }
        }
    }

    abstract fun provideUIEvents(): SharedFlow<Event>?

    /**
     * 处理UI事件
     */
    abstract fun handleUIEvents(event: Event)

}