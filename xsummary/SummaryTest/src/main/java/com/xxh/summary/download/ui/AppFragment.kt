package com.xxh.summary.download.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xxh.summary.R
import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.data.model.DownloadState
import com.xxh.summary.download.data.model.Status

class AppFragment : Fragment() {

    private val vm: AppViewModel by lazy {
        viewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app, container, false)
    }

    fun handleClick(app: AppInfo, state: DownloadState?) {
        when (state?.status) {
            null, Status.IDLE, Status.FAILED -> vm.download(app)
            Status.DOWNLOADING -> vm.pause(app.appId)
            Status.PAUSED -> vm.resume(app.appId)
            Status.SUCCESS -> openApp()
        }
    }

    fun a(){
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->

                val uiList = state.list.map { app ->
                    val ds = state.downloadMap[app.appId]
                    Pair(app, ds)
                }

                adapter.submitList(uiList)
            }
        }
    }

}