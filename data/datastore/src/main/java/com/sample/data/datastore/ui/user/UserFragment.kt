package com.sample.data.datastore.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.core.basic.ui.BaseFragment
import com.sample.data.datastore.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>() {

    private val userViewModel: UserViewModel by viewModels()

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        collectUiStates()
        userIntent()
    }

    private fun collectUiStates() {
        userViewModel.uiSettings.let {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    it.collect { state ->
                        mBinding.showTv.text = state.toString()
                    }
                }
            }
        }
    }

    fun userIntent() {
        mBinding.langSaveBtn.setOnClickListener {
            userViewModel.saveLang(mBinding.lang.text.toString())
        }

        mBinding.fontSizeSaveBtn.setOnClickListener {
            userViewModel.saveFontSize(mBinding.fontSize.text.toString())
        }
    }

}