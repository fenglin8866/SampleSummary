package com.sample.feature.upgrade.ui.state

import cn.nubia.upgrade.model.VersionData

sealed interface UpgradeUIState {
    data object Loading : UpgradeUIState
    data class Error(val errorId: Int) : UpgradeUIState
    data class Success(val versionData: VersionData?) : UpgradeUIState
    data class DownloadProgress(val progress: Int) : UpgradeUIState
    data object DownloadPause : UpgradeUIState
    data class DownloadComplete(val path: String) : UpgradeUIState
}