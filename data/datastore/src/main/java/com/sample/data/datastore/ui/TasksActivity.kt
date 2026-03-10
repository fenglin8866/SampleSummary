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

package com.sample.data.datastore.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.sample.data.datastore.data.SortOrder
import com.sample.data.datastore.data.TasksRepository
import com.sample.data.datastore.data.UserPreferencesRepository
import com.sample.data.datastore.data.dataStore
import com.sample.data.datastore.databinding.ActivityTasksBinding
import kotlinx.coroutines.launch

class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private val adapter = TasksAdapter()

    private lateinit var viewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTasksBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(
            this,
            TasksViewModelFactory(
                TasksRepository,
                UserPreferencesRepository(dataStore)
            )
        )[TasksViewModel::class.java]

        setupRecyclerView()

        viewModel.initialSetupEvent.observe(this) { initialSetupEvent ->
            Log.i("xxh1234", "initialSetupEvent tasksUiModel=${initialSetupEvent}")
            updateTaskFilters(initialSetupEvent.sortOrder, initialSetupEvent.showCompleted)
            setupOnCheckedChangeListeners()
            observePreferenceChanges()
        }
       // observePreferenceChanges()
    }

    private fun observePreferenceChanges() {
       /* viewModel.tasksUiModel.observe(this) { tasksUiModel ->
            adapter.submitList(tasksUiModel.tasks)
            updateTaskFilters(tasksUiModel.sortOrder, tasksUiModel.showCompleted)
        }*/
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.tasksUiModel2.collect { tasksUiModel ->
                   Log.i("xxh1234", "observePreferenceChanges tasksUiModel=${tasksUiModel}")
                   adapter.submitList(tasksUiModel.tasks)
                   updateTaskFilters(tasksUiModel.sortOrder, tasksUiModel.showCompleted)
               }
            }
        }
    }

    private fun setupRecyclerView() {
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        binding.list.adapter = adapter
    }

    private fun setupOnCheckedChangeListeners() {
        binding.sortDeadline.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners sortDeadline checked=$checked")
            viewModel.enableSortByDeadline(checked)
        }
        binding.sortPriority.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners sortPriority checked=$checked")
            viewModel.enableSortByPriority(checked)
        }
        binding.showCompletedSwitch.setOnCheckedChangeListener { _, checked ->
            Log.i("xxh1234", "setupOnCheckedChangeListeners showCompletedSwitch checked=$checked")
            viewModel.showCompletedTasks(checked)
        }
    }

    private fun updateTaskFilters(sortOrder: SortOrder, showCompleted: Boolean) {
        Log.i("xxh1234", "updateTaskFilters sortOrder=$sortOrder showCompleted=$showCompleted")
        // 先移除监听器，避免循环触发
        binding.sortDeadline.setOnCheckedChangeListener(null)
        binding.sortPriority.setOnCheckedChangeListener(null)
        binding.showCompletedSwitch.setOnCheckedChangeListener(null)

        binding.sortDeadline.isChecked =
            sortOrder == SortOrder.BY_DEADLINE || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        binding.sortPriority.isChecked =
            sortOrder == SortOrder.BY_PRIORITY || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        binding.showCompletedSwitch.isChecked = showCompleted

        // 恢复监听器
        setupOnCheckedChangeListeners()
    }
}
/*
stateFlow原因
collect 收到数据
    ↓
updateTaskFilters() 更新按钮状态（设置 isChecked）
    ↓
触发 OnCheckedChangeListener
    ↓
调用 viewModel.enableSortByDeadline() 等方法
    ↓
修改 DataStore → userPreferencesFlow 发射新值
    ↓
combine 重新计算 → tasksUiModel2 发射新值
    ↓
回到 collect，形成无限循环

LiveData原因
tasksUiModel.observe 收到数据变化
updateTaskFilters 更新 UI 控件状态
控件状态变化触发 OnCheckedChangeListener
调用 viewModel.enableSortByDeadline() 等方法
修改 DataStore 数据，触发 userPreferencesFlow 发射新值
combine 重新计算，导致 tasksUiModel 再次发射
回到步骤 1，形成循环
 */