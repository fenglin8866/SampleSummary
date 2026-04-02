package com.xxh.summary.download.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.data.repository.AppRepository
import com.xxh.summary.download.domain.DownloadManager
import com.xxh.summary.net.api.CmsApi
import com.xxh.summary.net.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.net.env.HostType
import com.xxh.summary.net.model.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repo: AppRepository
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())

    val state: StateFlow<AppListState> = combine(
        _apps,
        DownloadManager.stateFlow
    ) { apps, downloadMap ->
        AppListState(apps, downloadMap)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppListState()
    )

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            repo.loadApps().collect {
                when(it) {
                    is ResultState.Success -> _apps.value = it.data?.dataList ?: emptyList()
                    is ResultState.Error -> {}
                    else -> {}
                }
            }
        }
    }

    fun download(app: AppInfo) = repo.startDownload(app)

    fun pause(appId: String) = repo.pause(appId)

    fun resume(appId: String) = repo.resume(appId)

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