package com.sample.core.basic.event

import androidx.lifecycle.viewModelScope
import com.sample.core.basic.event.log.LogUIEvent
import kotlinx.coroutines.launch

/**
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

class MainViewModel : BaseViewModel() {

    fun onLoginExpired() {
        uiEventDispatcher.dispatch(
            MainUIEvent.NavigateLogin
        )
    }

    fun onListScrolledTop() {
        uiEventDispatcher.dispatch(
            LogUIEvent.OpenLogDir("adf")
        )
    }

    fun a(){
        viewModelScope.launch {
            uiEvents.collect {
                when (it) {
                    MainUIEvent.ScrollToTop -> ""
                    is MainUIEvent.Log -> Unit
                    is LogUIEvent.OpenLogDir -> Unit
                }
            }
        }
    }

    fun onError(msg: String) {
        uiEventDispatcher.dispatch(
            MainUIEvent.ShowToast(msg)
        )
    }
}



