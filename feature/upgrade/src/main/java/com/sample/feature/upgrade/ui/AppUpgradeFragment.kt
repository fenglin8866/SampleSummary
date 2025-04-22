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
import com.sample.core.basic.ui.event.UIEvent
import com.sample.core.basic.ui.view.clicks
import com.sample.feature.upgrade.R
import com.sample.feature.upgrade.ui.viewmodel.AppUpgradeViewModel
import com.sample.feature.upgrade.databinding.FragmentUpgradeBinding
import com.sample.feature.upgrade.ui.state.UpgradeUIIntent
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

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
            viewModel.uiEvent
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
       /* mBinding.apply {
            btnCheckNewVersion.setOnClickListener {
                viewModel.dispatchIntent(UpgradeUIIntent.OnCheckUpgradeClicked)
            }
            btnDownloadNewVersion.setOnClickListener {
                viewModel.dispatchIntent(UpgradeUIIntent.OnDownloadAppClicked)
            }
            btnInstallNewVersion.setOnClickListener {
                viewModel.dispatchIntent(UpgradeUIIntent.OnInstallAppClicked)
            }
            btnAppId.setOnClickListener {
                viewModel.dispatchIntent(UpgradeUIIntent.OnNavigateToAppId)
            }
        }*/
        val intentCheckVersionFlow = mBinding.btnCheckNewVersion.clicks().map {
            UpgradeUIIntent.OnCheckUpgradeClicked
        }

        val intentDownloadVersionFlow = mBinding.btnDownloadNewVersion.clicks().map {
            UpgradeUIIntent.OnDownloadAppClicked
        }

        val intentInstallVersionFlow = mBinding.btnInstallNewVersion.clicks().map {
            UpgradeUIIntent.OnInstallAppClicked
        }

        val intentNavigateAppIdFlow = mBinding.btnAppId.clicks().map {
            UpgradeUIIntent.OnNavigateToAppId
        }
        viewModel.consumeIntents(
            merge(
                intentCheckVersionFlow,
                intentDownloadVersionFlow,
                intentInstallVersionFlow,
                intentNavigateAppIdFlow
            )
        )
    }

}