package com.sample.feature.logger.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sample.feature.logger.basic.event.GlobalUiEvent
import com.sample.feature.logger.basic.event.GlobalUiEventDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: T? = null
    protected val binding get() = _binding!!

    @Inject
    lateinit var globalUiEventDispatcher: GlobalUiEventDispatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGlobalEvents()
        setupViews()
    }

    private fun observeGlobalEvents() {
        lifecycleScope.launch {
            globalUiEventDispatcher.events
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { event ->
                    handleGlobalEvent(event)
                }
        }
    }

    private fun handleGlobalEvent(event: GlobalUiEvent) {
        when (event) {
            is GlobalUiEvent.Toast -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }


    open fun setupViews() {

    }

    abstract fun bindView(inflater: LayoutInflater, container: ViewGroup?): T

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}