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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sample.data.datastore.data.SortOrder
import com.sample.data.datastore.data.Task
import com.sample.data.datastore.data.TasksRepository
import com.sample.data.datastore.data.UserPreferences
import com.sample.data.datastore.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TasksUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)

class TasksViewModel(
    repository: TasksRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * LiveData 被订阅时：当 Activity/Fragment 调用 observe() 开始观察，liveData { } 块内的代码才会执行，此时触发 emit
     *
     * 执行过程：
     * liveData { } 创建了一个 LiveData 对象
     * 订阅时自动启动协程执行块内代码
     * 调用 fetchInitialPreferences() 获取初始偏好（挂起函数，从 DataStore 读取）
     * 读取完成后 emit 发射结果
     * 只执行一次：由于使用 liveData 构建器，除非重新订阅，否则不会重复执行
     *
     * 注意：这段代码用于获取初始配置，发射一次后即结束。如需持续监听偏好变化，应使用 Flow 而非 liveData。
     */
    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    // Every time the sort order, the show completed filter or the list of tasks emit,
    // we should recreate the list of tasks
    private val tasksUiModelFlow = combine(
        repository.tasks,
        userPreferencesFlow
    ) { tasks: List<Task>, userPreferences: UserPreferences ->
        Log.i("xxh1234","tasksUiModelFlow showCompleted=${userPreferences.showCompleted} sortOrder=${userPreferences.sortOrder.name}")
        return@combine TasksUiModel(
            tasks = filterSortTasks(
                tasks,
                userPreferences.showCompleted,
                userPreferences.sortOrder
            ),
            showCompleted = userPreferences.showCompleted,
            sortOrder = userPreferences.sortOrder
        )
    }

    val tasksUiModel = tasksUiModelFlow.asLiveData()

    /**
     * SharingStarted.WhileSubscribed(5000)
     * 含义：
     * UI订阅 -> 开始上游Flow
     * UI取消 -> 5秒后停止
     * 避免：
     * Fragment重建
     * 重复网络请求
     */
    val tasksUiModel2: StateFlow<TasksUiModel> = tasksUiModelFlow
        .stateIn(
            scope = viewModelScope,           // ViewModel 的协程作用域
            started = SharingStarted.WhileSubscribed(5000),  // 订阅策略
            initialValue = TasksUiModel(      // 初始值
                tasks = emptyList(),
                showCompleted = false,
                sortOrder = SortOrder.NONE
            )
        )

    private fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        // filter the tasks
        val filteredTasks = if (showCompleted) {
            tasks
        } else {
            tasks.filter { !it.completed }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DEADLINE -> filteredTasks.sortedByDescending { it.deadline }
            SortOrder.BY_PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTasks.sortedWith(
                compareByDescending<Task> { it.deadline }.thenBy { it.priority }
            )
        }
    }

    fun showCompletedTasks(show: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateShowCompleted(show)
        }
    }

    fun enableSortByDeadline(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByDeadline(enable)
        }
    }

    fun enableSortByPriority(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByPriority(enable)
        }
    }
}

class TasksViewModelFactory(
    private val repository: TasksRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}