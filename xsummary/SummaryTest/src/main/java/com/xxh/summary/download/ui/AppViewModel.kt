package com.xxh.summary.download.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.xxh.summary.download.model.AppInfo
import com.xxh.summary.download.data.repository.AppRepository
import com.xxh.summary.download.data.repository.DownloadRepository
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.model.DownloadState
import com.xxh.summary.download.model.Status
import com.xxh.summary.net.api.CmsApi
import com.xxh.summary.net.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.net.env.HostType
import com.xxh.summary.net.model.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repo: AppRepository,
    private val downloadRepository: DownloadRepository = DownloadRepository
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())


    /*val uiList = combine(
        repo.loadApps(),
        DownloadRepository.stateFlow
    ) { list, stateMap ->

        list.map { app ->
            val state = stateMap[app.appId] ?: DownloadState(app.appId)
            AppUIModel(
                appId = app.appId,
                name = app.name,
                icon = app.icon,
                progress = state.progress,
                status = state.status
            )
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )*/


    val state = combine(
        _apps,
        downloadRepository.stateFlow
    ) { apps, downloadMap ->
        downloadMap.forEach { (string, state) ->
            Log.i("xxh123", "string: $string, state: $state")

        }
        apps.map { app ->
            val state = downloadMap[app.packageName] ?: DownloadState(app.packageName)
            AppUIModel(
                appId = app.appId,
                name = app.name,
                icon = app.icon,
                packageName = app.packageName,
                appSize = app.fileSize,
                summary = app.summary,
                progress = state.progress,
                status = state.status
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            repo.loadApps().collect {
                when (it) {
                    is ResultState.Success -> _apps.value = it.data?.dataList ?: emptyList()
                    is ResultState.Error -> {}
                    else -> {}
                }
            }
        }
    }

    fun handleClick(app: AppUIModel) {
        Log.i("xxh222", "handleClick status: ${app.status}")
        when (app.status) {
            Status.IDLE, Status.SUCCESS -> downloadRepository.start(app.packageName)
            Status.DOWNLOADING -> downloadRepository.pause(app.packageName)
            Status.PAUSED, Status.FAILED -> downloadRepository.resume(app.packageName)
            Status.INSTALLED -> downloadRepository.open(app.packageName)
            else -> {}
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val api = NetworkManagerMultiBaseUrl.create(HostType.CMS, CmsApi::class.java)
                val repository = AppRepository(api)
                return AppViewModel(
                    repository
                ) as T
            }
        }
    }

}