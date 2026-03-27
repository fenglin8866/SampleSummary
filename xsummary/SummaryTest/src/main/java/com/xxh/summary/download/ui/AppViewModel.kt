package com.xxh.summary.download.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.data.repository.AppRepository
import com.xxh.summary.download.domain.DownloadManager
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

    fun load() {
        viewModelScope.launch {
            _apps.value = repo.loadApps()
        }
    }

    fun download(app: AppInfo) = repo.startDownload(app)

    fun pause(appId: String) = repo.pause(appId)

    fun resume(appId: String) = repo.resume(appId)
}