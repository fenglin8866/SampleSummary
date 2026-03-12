package com.sample.data.datastore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sample.core.basic.ui.list.ListBaseFragment
import com.sample.core.basic2.fragment.BaseFragment
import com.sample.data.datastore.R
import com.sample.data.datastore.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun setup() {
        super.setup()
        binding.taskBtn.setOnClickListener {
            findNavController().navigate(Task)
        }

        binding.userBtn.setOnClickListener {
            findNavController().navigate(User)
        }
    }
}