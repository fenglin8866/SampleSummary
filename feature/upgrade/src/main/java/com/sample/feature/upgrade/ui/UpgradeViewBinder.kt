package com.sample.feature.upgrade.ui

import android.graphics.Color
import cn.nubia.upgrade.model.VersionData
import com.sample.core.basic.ui.view.ViewStateBinder
import com.sample.feature.upgrade.databinding.FragmentUpgradeBinding
import com.sample.feature.upgrade.ui.state.UpgradeUIState

class UpgradeViewBinder : ViewStateBinder<FragmentUpgradeBinding, UpgradeUIState> {
    override fun bind(binding: FragmentUpgradeBinding, state: UpgradeUIState) {
        when (state) {
            is UpgradeUIState.Loading -> showLoading()
            is UpgradeUIState.Success -> showSuccess(binding, state.versionData)
            is UpgradeUIState.Error -> showError(state.errorId)
            is UpgradeUIState.DownloadComplete -> showDownloadComplete(binding, state.path)
            is UpgradeUIState.DownloadPause -> showDownloadPause()
            is UpgradeUIState.DownloadProgress -> showDownloadProgress(binding, state.progress)
        }
    }

    // 显示成功状态
    private fun showSuccess(mBinding: FragmentUpgradeBinding, versionData: VersionData?) {
        mBinding.apply {
            if (versionData != null) {
                txtCheckNewVersion.text = versionData.upgradeContent
                btnDownloadNewVersion.isClickable = true
                btnDownloadNewVersion.isEnabled = true
                btnDownloadNewVersion.setTextColor(Color.BLUE)
            } else {
                txtCheckNewVersion.text = "no new version."
            }
        }

    }

    private fun showDownloadProgress(mBinding: FragmentUpgradeBinding, progress: Int) {
        mBinding.apply {
            barDownloadNewVersion.progress = progress
            txtDownloadNewVersion.setText("downloading...$progress%")
        }

    }

    private fun showDownloadComplete(mBinding: FragmentUpgradeBinding, path: String) {
        mBinding.apply {
            barDownloadNewVersion.setProgress(100)
            txtDownloadNewVersion.setText("download finish.\nfile_path:$path")

            btnDownloadNewVersionStatus.setClickable(false)
            btnDownloadNewVersionStatus.setEnabled(false)
            btnDownloadNewVersionStatus.setTextColor(Color.GRAY)

            btnInstallNewVersion.setClickable(true)
            btnInstallNewVersion.setEnabled(true)
            btnInstallNewVersion.setTextColor(Color.BLUE)
        }

    }

    // 显示加载动画
    private fun showLoading() {

    }

    private fun showDownloadPause() {

    }

    // 显示错误信息
    private fun showError(errorCode: Int) {


    }
}