/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.data.datastore.ui.task

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.sample.core.basic.ui.BaseFragment
import com.sample.data.datastore.databinding.FragmentTasksBinding
import com.sample.data.datastore.proto.UserPreferences.SortOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : BaseFragment<FragmentTasksBinding>() {

    private val adapter = TasksAdapter()

    private val viewModel: TasksViewModel by viewModels()

    private var isInit = false

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTasksBinding {
       return FragmentTasksBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        setupRecyclerView()
        collectUiEvents()
    }

    private fun collectUiEvents() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasksUiState.collect { state ->
                    when (state) {
                        is TasksUiState.Success -> {
                            val tasksUiModel = state.model
                            adapter.submitList(tasksUiModel.tasks)
                            updateTaskFilters(tasksUiModel.sortOrder, tasksUiModel.showCompleted)
                            userIntent()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        mBinding.list.addItemDecoration(decoration)
        mBinding.list.adapter = adapter
    }

    private fun userIntent() {
        if (isInit) {
            return
        }
        isInit = true
        mBinding.sortDeadline.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners sortDeadline checked=$checked")
            viewModel.enableSortByDeadline(checked)
        }
        mBinding.sortPriority.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners sortPriority checked=$checked")
            viewModel.enableSortByPriority(checked)
        }
        mBinding.showCompletedSwitch.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners showCompletedSwitch checked=$checked")
            viewModel.showCompletedTasks(checked)
        }
    }

    private fun updateTaskFilters(sortOrder: SortOrder, showCompleted: Boolean) {
        mBinding.sortDeadline.isChecked =
            sortOrder == SortOrder.BY_DEADLINE || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        mBinding.sortPriority.isChecked =
            sortOrder == SortOrder.BY_PRIORITY || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        mBinding.showCompletedSwitch.isChecked = showCompleted
    }
}