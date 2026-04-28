package com.xxh.summary.download.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.sample.lib.network.model.ResultState
import com.xxh.summary.download.data.repository.DownloadRepository
import com.xxh.summary.download.model.AppListResponse
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.model.AppUiState
import com.xxh.summary.download.model.ClickType
import com.xxh.summary.download.model.DownloadState
import com.xxh.summary.download.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DownloadAppUserCase {

    fun getShowAppsFlow(
        apiResult: Flow<ResultState<AppListResponse>>,
        scope: CoroutineScope
    ): StateFlow<AppUiState> {
        return combine(
            apiResult,
            DownloadRepository.stateFlow
        )
        { result, downloadMap ->
            when (result) {
                is ResultState.Success -> {
                    val apps = result.data?.dataList ?: emptyList()
                    if (apps.isEmpty()) {
                        AppUiState.Error("No data")
                    } else {
                        val model = apps.map { app ->
                            val state =
                                downloadMap[app.packageName] ?: DownloadState(app.packageName)
                            AppUIModel(
                                app.appId,
                                app.name,
                                app.icon,
                                app.packageName,
                                app.appSize,
                                app.summary,
                                app.downloadUrl,
                                app.detailUrl,
                                state.progress,
                                state.status
                            )
                        }
                        AppUiState.Success(model)
                    }
                }

                is ResultState.Error -> AppUiState.Error(result.error.message ?: "")
                is ResultState.Loading -> AppUiState.Loading
                is ResultState.Idle -> AppUiState.Idle
            }
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000),
            AppUiState.Loading
        )
    }

    fun handleAction(type: ClickType, app: AppUIModel) {
        if (isInstalled(app.packageName)) {
            DownloadRepository.open(app.packageName)
        } else {
            when (type) {
                ClickType.ITEM -> clickItem(app.packageName, app.detailUrl)
                ClickType.DOWNLOAD -> clickDownload(
                    app.packageName,
                    app.downloadUrl,
                    app.status
                )
            }
        }
    }

    fun clickItem(packageName: String, detailUrl: String) {
        jumpDeepLink(detailUrl)
        registerDetailDownloadListener(packageName)
    }

    fun clickDownload(packageName: String, downloadUrl: String, state: Status) {
        if (isSupportDownload()) {
            when (state) {
                Status.IDLE, Status.SUCCESS -> DownloadRepository.start(packageName)
                Status.DOWNLOADING -> DownloadRepository.pause(packageName)
                Status.PAUSED -> DownloadRepository.resume(packageName)
                Status.FAILED -> DownloadRepository.start(packageName)
                Status.INSTALLED -> DownloadRepository.open(packageName)
                else -> Unit
            }
        } else {
            jumpDeepLink(downloadUrl)
        }
    }


    fun isSupportDownload(): Boolean {
        return DownloadRepository.isDownloadEnabled()
    }

    fun isInstalled(packageName: String): Boolean {
        return false
    }

    fun jumpDeepLink(url: String) {

    }

    fun downloadCallback(downloadApps: ((Map<String, DownloadState>) -> Unit)?) {
        DownloadRepository.downloadCallback(downloadApps)
    }

    fun getDownloadStateLiveData(): LiveData<Map<String, DownloadState>> {
        return DownloadRepository.stateFlow.asLiveData()
    }

    fun registerDetailDownloadListener(packageName: String?) {
        if (isSupportDownload() && !packageName.isNullOrEmpty()) {
            DownloadRepository.addAppDownloadListener(packageName)
        }
    }
}