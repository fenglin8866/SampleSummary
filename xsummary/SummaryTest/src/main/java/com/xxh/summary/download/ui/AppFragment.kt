package com.xxh.summary.download.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.core.basic2.fragment.BaseStateFragment
import com.xxh.summary.databinding.FragmentAppBinding
import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.ui.adapter.AppAdapter
import kotlinx.coroutines.flow.StateFlow
import androidx.fragment.app.viewModels

class AppFragment : BaseStateFragment<FragmentAppBinding, AppListState>() {


    private  var appAdapter: AppAdapter?=null
    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    private var searchResults = mutableListOf<AppInfo>()
    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAppBinding {
        return FragmentAppBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        appAdapter = AppAdapter(
            searchResults
        ){
            viewModel.download(it)
        }
        binding.appList.adapter = appAdapter
        binding.appList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun provideUIState(): StateFlow<AppListState> {
        return viewModel.state
    }

    override fun render(state: AppListState) {
        Log.i("xxh123","render state: ${state.downloadMap}")
        if (state.list.isNotEmpty()) {
            searchResults.clear()
            searchResults.addAll(state.list)
            appAdapter?.updateAppList(searchResults)
        }
    }

}