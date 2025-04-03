package com.sample.feature.upgrade

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cn.nubia.upgrade.model.VersionData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.sample.feature.upgrade.databinding.FragmentMainBinding

class AppUpgradeFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: AppUpgradeViewModel by activityViewModels()

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UpgradeUiState.Loading -> showLoading()
                    is UpgradeUiState.Success -> showSuccess(state.versionData)
                    is UpgradeUiState.Error -> showError(state.errorId)
                    is UpgradeUiState.DownloadComplete -> showDownloadComplete(state.path)
                    is UpgradeUiState.DownloadPause -> showDownloadPause()
                    is UpgradeUiState.DownloadProgress -> showDownloadProgress(state.progress)
                }
            }
        }
        mBinding.apply {

            txtVersionInfo.text = viewModel.getAppVersionInfo()

            btnCheckNewVersion.setOnClickListener {
                viewModel.checkUpgrade()
            }
            btnDownloadNewVersion.setOnClickListener {
                viewModel.downloadApp()
            }
            btnInstallNewVersion.setOnClickListener {
                viewModel.installApp()
            }
            btnAppId.setOnClickListener {
                findNavController().navigate("https://test.upgrade.cn/${viewModel.getAppId()}".toUri())
            }
        }
    }

    // 显示加载动画
    private fun showLoading() {

    }

    // 显示成功状态
    private fun showSuccess(versionData: VersionData?) {
        mBinding.apply {
            if (versionData != null) {
                txtCheckNewVersion.text = versionData.upgradeContent
                btnDownloadNewVersion.isClickable = true
                btnDownloadNewVersion.isEnabled = true
                btnDownloadNewVersion.setTextColor(Color.BLUE)
                showUpgradeDialog()
            } else {
                txtCheckNewVersion.text = "no new version."
            }
        }

    }

    private fun showUpgradeDialog() {
        findNavController().navigate(R.id.action_to_appUpgradeDialog)
    }

    // 显示错误信息
    private fun showError(errorCode: Int) {


    }

    private fun showDownloadProgress(progress: Int) {
        mBinding.apply {
            barDownloadNewVersion.progress = progress
            txtDownloadNewVersion.setText("downloading...$progress%")
        }

    }

    private fun showDownloadComplete(path: String) {
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

    private fun showDownloadPause() {

    }


}