package com.sample.core.basic2.fragment

import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.sample.core.basic2.event.AppUIEvent
import com.sample.core.basic2.event.DefaultUiEvent
import com.sample.core.basic2.event.UIEvent

abstract class BaseStateDefaultEventFragment<VB : ViewBinding, State : Any> :
    BaseStateEventFragment<VB, State, UIEvent>() {


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

            is DefaultUiEvent.Navigate -> handleNavigateEvent(event)
            is DefaultUiEvent.Snackbar -> handleSnackbarEvent(event)
            is DefaultUiEvent.Log -> handleLogEvent(event)
            DefaultUiEvent.Back -> handleBackEvent()
            DefaultUiEvent.Dialog -> handleDialogEvent()
            DefaultUiEvent.ScrollToTop -> handleScrollToTopEvent()
        }

    }

    protected open fun handleNavigateEvent(event: DefaultUiEvent.Navigate) {
        // 子类可以选择重写此方法来处理导航事件
    }

    protected open fun handleSnackbarEvent(event: DefaultUiEvent.Snackbar) {
        // 子类可以选择重写此方法来处理 Snackbar 事件
    }

    protected open fun handleLogEvent(event: DefaultUiEvent.Log) {
        // 子类可以选择重写此方法来处理日志事件
    }

    protected open fun handleBackEvent() {
        // 子类可以选择重写此方法来处理返回事件
        requireActivity().onBackPressed()
    }

    protected open fun handleDialogEvent() {
        // 子类可以选择重写此方法来处理对话框事件
    }

    protected open fun handleScrollToTopEvent() {
        // 子类可以选择重写此方法来处理滚动到顶部事件
    }

}