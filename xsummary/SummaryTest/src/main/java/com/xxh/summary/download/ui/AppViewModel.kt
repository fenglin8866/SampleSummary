package com.xxh.summary.download.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.domain.DownloadAppUseCase
import com.xxh.summary.download.model.ClickType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userCase: DownloadAppUseCase,
   // private val appRepository: AppRepository
) : ViewModel() {

   /* // 缓存 API 结果，避免重复请求
    private val apiResult = appRepository.loadTopApps()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ResultState.Loading)*/


    val state = userCase.getShowAppsFlow(viewModelScope)

    fun handleAction(type: ClickType, app: AppUIModel) {
        Log.i("xxh222", "handleClick status: ${app.status}")
        userCase.handleAction(type, app)
    }
}