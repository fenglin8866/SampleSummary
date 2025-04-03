package com.sample.feature.upgrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.nubia.upgrade.model.VersionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppUpgradeViewModel @Inject constructor(
    private val appUpgradeManager: AppUpgradeManager
) : ViewModel() {

    private var _versionData: MutableLiveData<VersionData> = MutableLiveData()

    val mVersionData: LiveData<VersionData> = _versionData

    private val _uiState = MutableStateFlow<UpgradeUiState>(UpgradeUiState.Loading)

    val uiState: StateFlow<UpgradeUiState> = _uiState.asStateFlow()

    private fun updateVersionData(data: VersionData) {
        _versionData.value = data
    }

    private fun updateState(newValue: UpgradeUiState) {
        _uiState.value = newValue
    }

    fun checkUpgrade() {
        appUpgradeManager.checkUpdate { versionData, error ->
            if (versionData != null) {
                updateState(UpgradeUiState.Success(versionData))
                updateVersionData(versionData)
            } else if (error != null) {
                updateState(UpgradeUiState.Error(error))
            } else {
                updateState(UpgradeUiState.Success(null))
            }
        }
    }

    fun isApkExist(): Boolean {
        return appUpgradeManager.isApkExist()
    }

    fun downloadApp() {
        appUpgradeManager.downloadApp { progress, path ->
            if (progress != null) {
                updateState(UpgradeUiState.DownloadProgress(progress))
            }
            if (path != null) {
                updateState(UpgradeUiState.DownloadComplete(path))
            }
        }
    }

    fun installApp() {
        appUpgradeManager.installApp()
    }

    fun getAppId(): String {
        return appUpgradeManager.getAppId()
    }

    fun getAppVersionInfo(): String {
        return appUpgradeManager.getAppVersionInfo()
    }

}

sealed interface UpgradeUiState {
    data object Loading : UpgradeUiState
    data class Error(val errorId: Int) : UpgradeUiState
    data class Success(val versionData: VersionData?) : UpgradeUiState
    data class DownloadProgress(val progress: Int) : UpgradeUiState
    data object DownloadPause : UpgradeUiState
    data class DownloadComplete(val path: String) : UpgradeUiState
}
