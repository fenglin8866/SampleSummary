package com.xxh.summary.download.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.domain.DownloadAppUserCase
import com.xxh.summary.download.model.ClickType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userCase: DownloadAppUserCase
) : ViewModel() {

    val state = userCase.getShowAppsFlow(viewModelScope)

    fun handleAction(type: ClickType, app: AppUIModel) {
        Log.i("xxh222", "handleClick status: ${app.status}")
        userCase.handleAction(type, app)
    }
}