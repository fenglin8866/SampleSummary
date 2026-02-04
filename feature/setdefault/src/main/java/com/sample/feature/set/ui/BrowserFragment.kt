package com.sample.feature.set.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sample.feature.set.infra.DefaultBrowserLauncher
import com.sample.feature.set.viewmodel.BrowserViewModel

class BrowserFragment : Fragment() {

    private val viewModel: BrowserViewModel by viewModels()

    private lateinit var launcher: DefaultBrowserLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = DefaultBrowserLauncher(
            requireActivity()
        ) {
            viewModel.onRoleReturnFast()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        collectUiEvent()
        setupWebView()
    }

    private fun collectUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    BrowserUiEvent.ShowDefaultBrowserGuide ->
                        showGuideDialog()

                    BrowserUiEvent.LaunchRoleManager ->
                        launcher.launch()

                    BrowserUiEvent.OpenDefaultBrowserSettings ->
                        openDefaultBrowserSettings()
                }
            }
        }
    }

    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(
                view: WebView?,
                url: String?
            ) {
                viewModel.onPageLoaded(requireContext())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(requireContext())
    }
}
