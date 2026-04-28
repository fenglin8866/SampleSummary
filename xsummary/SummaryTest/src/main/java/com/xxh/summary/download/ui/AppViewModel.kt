package com.xxh.summary.download.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.download.data.repository.AppRepository
import com.xxh.summary.download.data.repository.DownloadRepository
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.model.Status
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.config.HostManager
import com.xxh.summary.download.domain.DownloadAppUserCase

class AppViewModel(
    private val repo: AppRepository,
    private val downloadRepository: DownloadRepository = DownloadRepository
) : ViewModel() {

    val state = DownloadAppUserCase().getShowAppsFlow(repo.loadTopApps(), viewModelScope)

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
                val api = NetworkManagerMultiBaseUrl.create(HostManager.getAppBaseUrl(), AppApi::class.java)
                val repository = AppRepository(api)
                return AppViewModel(
                    repository
                ) as T
            }
        }
    }

}