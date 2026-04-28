package com.xxh.summary.download.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.lib.network.model.ResultState
import com.xxh.summary.download.data.repository.AppRepository
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.domain.DownloadAppUserCase
import com.xxh.summary.download.model.ClickType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userCase: DownloadAppUserCase,
    private val appRepository: AppRepository
) : ViewModel() {

    // 缓存 API 结果，避免重复请求
    private val apiResult = appRepository.loadTopApps()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ResultState.Loading)


    val state = userCase.getShowAppsFlow(viewModelScope, apiResult)

    fun handleAction(type: ClickType, app: AppUIModel) {
        Log.i("xxh222", "handleClick status: ${app.status}")
        userCase.handleAction(type, app)
    }
}