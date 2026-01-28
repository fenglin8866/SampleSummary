package com.sample.core.basic.event2

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sample.core.basic.ui.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseGlobalEventFragment<T : ViewBinding> : BaseFragment<T>() {

    @Inject
    lateinit var globalUiEventDispatcher: GlobalUiEventDispatcher


    private fun collectEvents() {
        lifecycleScope.launch {
            globalUiEventDispatcher.events
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { event ->
                    when (event) {
                        is GlobalUiEvent.Toast -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> Unit
                    }
                }
        }
    }


}