package com.sample.feature.upgrade.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.nubia.upgrade.model.VersionData
import com.sample.core.basic.ui.BaseViewModel
import com.sample.core.basic.ui.UIEvent
import com.sample.feature.upgrade.AppUpgradeManager
import com.sample.feature.upgrade.ui.state.UpgradeUIIntent
import com.sample.feature.upgrade.ui.state.UpgradeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppUpgradeViewModel @Inject constructor(
    private val appUpgradeManager: AppUpgradeManager
) : BaseViewModel<UpgradeUIState, UpgradeUIIntent>(UpgradeUIState.Loading) {

    private var _versionData: MutableLiveData<VersionData> = MutableLiveData()

    val mVersionData: LiveData<VersionData> = _versionData

    fun isApkExist(): Boolean {
        return appUpgradeManager.isApkExist()
    }

    fun getAppVersionInfo(): String {
        return appUpgradeManager.getAppVersionInfo()
    }

    private fun updateVersionData(data: VersionData) {
        _versionData.value = data
    }

    private fun getAppId(): String {
        return appUpgradeManager.getAppId()
    }

    override fun handleIntent(intent: UpgradeUIIntent) {
        when (intent) {
            UpgradeUIIntent.OnCheckUpgradeClicked -> checkUpgrade()
            UpgradeUIIntent.OnDownloadAppClicked -> downloadApp()
            UpgradeUIIntent.OnInstallAppClicked -> installApp()
            UpgradeUIIntent.OnNavigateToAppId -> navigateToAppId()
        }
    }

    private fun checkUpgrade() {
        appUpgradeManager.checkUpdate { versionData, error ->
            if (versionData != null) {
                updateState(UpgradeUIState.Success(versionData))
                updateVersionData(versionData)
                sendEvent(UIEvent.Dialog)
            } else if (error != null) {
                updateState(UpgradeUIState.Error(error))
            } else {
                updateState(UpgradeUIState.Success(null))
            }
        }
    }

    private fun downloadApp() {
        appUpgradeManager.downloadApp { progress, path ->
            if (progress != null) {
                updateState(UpgradeUIState.DownloadProgress(progress))
            }
            if (path != null) {
                updateState(UpgradeUIState.DownloadComplete(path))
            }
        }
    }

    private fun installApp() {
        appUpgradeManager.installApp()
    }

    private fun navigateToAppId() {
        sendEvent(UIEvent.Navigate("https://test.upgrade.cn/${getAppId()}"))
    }

}
