package com.sample.feature.upgrade.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.sample.core.basic.ui.BaseFragment
import com.sample.core.basic.ui.UIEvent
import com.sample.feature.upgrade.R
import com.sample.feature.upgrade.ui.viewmodel.AppUpgradeViewModel
import com.sample.feature.upgrade.databinding.FragmentUpgradeBinding
import com.sample.feature.upgrade.ui.state.UpgradeUIIntent

class AppUpgradeFragment : BaseFragment<FragmentUpgradeBinding>() {

    private val viewModel: AppUpgradeViewModel by activityViewModels()

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): FragmentUpgradeBinding {
        return FragmentUpgradeBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        initView()
        observeState()
        observeEvents()
        userIntent()
    }

    private fun initView() {
        mBinding.txtVersionInfo.text = viewModel.getAppVersionInfo()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    UpgradeViewBinder().bind(mBinding, state)
                }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { event ->
                    when (event) {
                        UIEvent.Dialog -> findNavController().navigate(R.id.action_to_appUpgradeDialog)
                        is UIEvent.Navigate -> findNavController().navigate(event.route.toUri())
                        else -> {

                        }
                    }
                }
        }
    }

    private fun userIntent() {
        mBinding.apply {
            btnCheckNewVersion.setOnClickListener {
                viewModel.handleIntent(UpgradeUIIntent.OnCheckUpgradeClicked)
            }
            btnDownloadNewVersion.setOnClickListener {
                viewModel.handleIntent(UpgradeUIIntent.OnDownloadAppClicked)
            }
            btnInstallNewVersion.setOnClickListener {
                viewModel.handleIntent(UpgradeUIIntent.OnInstallAppClicked)
            }
            btnAppId.setOnClickListener {
                viewModel.handleIntent(UpgradeUIIntent.OnNavigateToAppId)
            }
        }
    }

}