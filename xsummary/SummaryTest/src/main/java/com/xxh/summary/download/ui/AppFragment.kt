package com.xxh.summary.download.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.core.basic2.fragment.BaseStateFragment
import com.xxh.summary.databinding.FragmentAppBinding
import kotlinx.coroutines.flow.StateFlow
import androidx.fragment.app.viewModels
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.ui.adapter.AppAdapter

class AppFragment : BaseStateFragment<FragmentAppBinding, List<AppUIModel>>() {

    private var appAdapter: AppAdapter? = null
    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAppBinding {
        return FragmentAppBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        appAdapter = AppAdapter {
            viewModel.handleClick(it)
        }
        binding.appList.adapter = appAdapter
        binding.appList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun provideUIState(): StateFlow<List<AppUIModel>> {
        return viewModel.state
    }

    override fun render(state: List<AppUIModel>) {
        appAdapter?.submitList(state)
    }


}