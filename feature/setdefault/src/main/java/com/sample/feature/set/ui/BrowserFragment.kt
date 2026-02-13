package com.sample.feature.set.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.sample.core.basic2.fragment.BaseStateAndEventFragment
import com.sample.feature.set.databinding.FragmentWebviewBinding
import com.sample.feature.set.infra.DefaultBrowserLauncher
import com.sample.feature.set.ui.contract.BrowserUiEvent
import com.sample.feature.set.ui.contract.BrowserUiState
import com.sample.feature.set.ui.viewmodel.BrowserViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class BrowserFragment :
    BaseStateAndEventFragment<FragmentWebviewBinding, BrowserUiState, BrowserUiEvent>() {

    private val viewModel: BrowserViewModel by viewModels {
        BrowserViewModel.Factory
    }

    /**
     * UI相关逻辑处理
     * 默认浏览器设置工具类
     */
    private lateinit var launcher: DefaultBrowserLauncher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = DefaultBrowserLauncher(requireActivity())
    }

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebviewBinding {
        return FragmentWebviewBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(
                view: WebView?,
                url: String?
            ) {
                viewModel.onPageLoaded(requireContext())
            }
        }
    }

    override fun userIntent() {
        super.userIntent()
        binding.setDefault.setOnClickListener {
            viewModel.onDefaultDialog()
        }
        binding.loadUrl.setOnClickListener {
            viewModel.onPageLoaded(requireContext())
        }
    }

    override fun provideUIEvents(): SharedFlow<BrowserUiEvent> {
        return viewModel.uiEvent
    }

    override fun handleUIEvents(event: BrowserUiEvent) {
        when (event) {
            BrowserUiEvent.ShowDefaultBrowserGuide ->
                showGuideDialog()

            BrowserUiEvent.LaunchRoleManager ->
                launcher.launch()

        }
    }

    override fun provideUIState(): StateFlow<BrowserUiState> {
        return viewModel.uiState
    }

    override fun render(state: BrowserUiState) {
        if(state.isDefaultBrowser){
            binding.setDefault.text = "已设置"
        }
    }

    private fun showGuideDialog() {
        //构建有按钮的一个弹窗
        val guideDialog = GuideDialog(requireContext())
        guideDialog.setOnPositiveButtonClickListener(object : OnPositiveButtonClickListener {
            override fun onPositiveClick() {
                // 处理“确定”按钮点击逻辑
                viewModel.onDefaultDialog()
            }
        })
        guideDialog.show()
    }


    override fun onResume() {
        super.onResume()
        viewModel.onResume(requireContext())
    }
}
