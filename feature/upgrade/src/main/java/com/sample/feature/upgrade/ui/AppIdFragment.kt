package com.sample.feature.upgrade.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sample.core.basic.ui.BaseFragment
import com.sample.feature.upgrade.databinding.FragmentAppIdBinding


class AppIdFragment : BaseFragment<FragmentAppIdBinding>() {

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): FragmentAppIdBinding {
        return FragmentAppIdBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        val args = arguments?.getString("appId")
        args?.let {
            mBinding.appIdEv.setText(it)
        }
    }

}
