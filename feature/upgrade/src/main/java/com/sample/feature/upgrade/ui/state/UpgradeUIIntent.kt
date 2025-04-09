package com.sample.feature.upgrade.ui.state

sealed class UpgradeUIIntent {
    object OnCheckUpgradeClicked : UpgradeUIIntent()
    object OnDownloadAppClicked : UpgradeUIIntent()
    object OnInstallAppClicked : UpgradeUIIntent()
    object OnNavigateToAppId : UpgradeUIIntent()
}