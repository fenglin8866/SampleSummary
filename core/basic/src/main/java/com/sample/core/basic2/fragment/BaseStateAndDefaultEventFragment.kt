package com.sample.core.basic2.fragment

import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.sample.core.basic2.event.AppUIEvent
import com.sample.core.basic2.event.DefaultUiEvent
import com.sample.core.basic2.event.UIEvent

abstract class BaseStateAndDefaultEventFragment<T : ViewBinding, State : Any> :
    BaseStateAndEventFragment<T, State, UIEvent>() {


    override fun handleUIEvents(event: UIEvent) {
        when (event) {
            is AppUIEvent -> handleDefaultEvents(event)
            else -> Unit
        }
    }

    /**
     * 统一处理模块UI事件
     */
    abstract fun handleModuleEvents(event: UIEvent)

    protected fun handleDefaultEvents(event: AppUIEvent) {
        when (event) {
            is DefaultUiEvent.Toast -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }

            is DefaultUiEvent.Navigate -> TODO()
            is DefaultUiEvent.Snackbar -> TODO()
            is DefaultUiEvent.Log -> TODO()
            DefaultUiEvent.Back -> TODO()
            DefaultUiEvent.Dialog -> TODO()
            DefaultUiEvent.ScrollToTop -> TODO()
        }

    }

}