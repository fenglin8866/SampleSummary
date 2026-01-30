/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.sample.feature.logger.logs.repository.data

import com.sample.feature.logger.logs.repository.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class LogInMemoryDataSource @Inject constructor() : LogDataSource {

    private val _logList = MutableStateFlow<List<Log>>(emptyList())
    val logList: StateFlow<List<Log>> = _logList.asStateFlow()

    override suspend fun addLog(log: Log) {
        val currentList = _logList.value.toMutableList()
        currentList.add(log)
        _logList.value = currentList
    }

    suspend fun addLog2(log: Log) {
        _logList.update { currentList ->
            val newList = currentList.toMutableList()
            if (newList.size >= MAX_LOG_COUNT) {
                // 移除最旧的日志以保持数量限制
                newList.removeAt(0)
            }
            newList.add(log)
            newList
        }
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(_logList.value)
    }

    override suspend fun removeLogs() {
        _logList.value = emptyList()
    }

    override fun getAllLogs(): Flow<List<Log>> {
        return logList
    }

    // 设置最大日志数量限制，防止内存溢出
    private companion object {
        const val MAX_LOG_COUNT = 1000
    }
}