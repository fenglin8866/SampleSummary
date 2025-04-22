package com.sample.feature.upgrade.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sample.feature.upgrade.ui.viewmodel.AppUpgradeViewModel
import com.sample.feature.upgrade.databinding.FragmentDialogUpgradeBinding
import com.sample.feature.upgrade.ui.state.UpgradeUIIntent
import kotlin.system.exitProcess

class AppUpgradeDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogUpgradeBinding? = null
    private val mBinding get() = _binding!!

    private val viewModel: AppUpgradeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogUpgradeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(1000, 700) // 900px 宽, 600px 高
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setupViews()
    }

    private fun setupViews() {
        viewModel.mVersionData.observe(this) { data ->
            mBinding.apply {
                content.text = data.upgradeContent

                doneButton.apply {
                    text = if (viewModel.isApkExist()) {
                        "安装"
                    } else {
                        "更新"
                    }
                    setOnClickListener {
                        if (viewModel.isApkExist()) {
                            viewModel.dispatchIntent(UpgradeUIIntent.OnInstallAppClicked)
                        } else {
                            viewModel.dispatchIntent(UpgradeUIIntent.OnDownloadAppClicked)
                        }
                        findNavController().popBackStack()
                    }
                }

                cancelButton.setOnClickListener {
                    findNavController().popBackStack()
                    if (data.isForce) {
                        activity?.finishAffinity()
                        exitProcess(0)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}