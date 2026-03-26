package com.sample.core.basic2.viewmodel

import androidx.lifecycle.viewModelScope
import com.sample.core.basic2.event.DefaultUIEventDispatcher
import com.sample.core.basic2.event.UIEvent
import kotlinx.coroutines.flow.SharedFlow

/**
 * 统一处理UI事件的ViewModel基类
 * 该类结合了Intent处理能力和默认UI事件分发器
 *
 * 优势：
 * - 自动处理常见UI事件（Toast, Navigation, Snackbar等）
 * - 统一的事件处理方式
 * - 减少样板代码
 *
 * 使用示例：
 * 功能ViewModel
 * fun onListScrolledTop() {
 *    uiEventDispatcher.dispatch(
 *             GlobalUiEvent.ScrollToTop
 *      )
 * }
 *
 * fun onError(msg: String) {
 *    uiEventDispatcher.dispatch(
 *             GlobalUiEvent.Toast(msg)
 *      )
 * }
 *
 * Activity / Fragment 收事件
 *lifecycleScope.launch {
 *     repeatOnLifecycle(Lifecycle.State.STARTED) {
 *         viewModel.uiEvents.collect { event ->
 *             when (event) {
 *                 is MainUIEvent.ShowToast ->
 *                     Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
 *
 *                 MainUIEvent.NavigateLogin ->
 *                     findNavController().navigate(R.id.login)
 *
 *                 MainUIEvent.ScrollToTop ->
 *                     recyclerView.scrollToPosition(0)
 *
 *                 is MainUIEvent.Log ->
 *                     Log.d("UI", event.text)
 *             }
 *         }
 *     }
 * }
 */
/**
 * 统一处理UI事件
 * 处理的事件包含功能模块UI事件和全局UI事件
 * uiEvent不是密封类型，注意else->Unit的处理，但不要遗漏相关分支。
 */
abstract class BaseDefaultEventViewModel<State : Any, Intent : Any>(initialState: State) :
    BaseIntentViewModel<State, Intent>(initialState) {

    protected val uiEventDispatcher = DefaultUIEventDispatcher<UIEvent>(viewModelScope)

    val uiEvent: SharedFlow<UIEvent>
        get() = uiEventDispatcher.events

    protected fun sendEvent(event: UIEvent) {
        uiEventDispatcher.dispatch(event)
    }

}
