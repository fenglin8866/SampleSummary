package com.sample.core.basic2.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


abstract class BaseStateAndEventFragment<T : ViewBinding, State : Any, Event : Any> :
    BaseStateFragment<T, State>() {

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