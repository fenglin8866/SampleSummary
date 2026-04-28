package com.xxh.summary.download.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.core.basic2.fragment.BaseStateFragment
import com.xxh.summary.databinding.FragmentAppBinding
import androidx.fragment.app.viewModels
import com.xxh.summary.download.model.AppUiState
import com.xxh.summary.download.ui.adapter.AppAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class AppFragment : BaseStateFragment<FragmentAppBinding, AppUiState>() {

    private var appAdapter: AppAdapter? = null
    private val viewModel: AppViewModel by viewModels()

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAppBinding {
        return FragmentAppBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        appAdapter = AppAdapter { type, model ->
            viewModel.handleAction(type, model)
        }
        binding.appList.adapter = appAdapter
        binding.appList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun provideUIState(): StateFlow<AppUiState> {
        return viewModel.state
    }

    override fun render(state: AppUiState) {
        when (state) {
            is AppUiState.Success -> {
                appAdapter?.submitList(state.data)
            }

            is AppUiState.Error -> {
                // Handle error
            }

            is AppUiState.Loading -> {
                // Handle loading
            }

            else -> Unit
        }
    }

}