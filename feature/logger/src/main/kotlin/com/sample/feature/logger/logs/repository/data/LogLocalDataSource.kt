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

import androidx.lifecycle.asLiveData
import com.sample.feature.logger.logs.repository.Log
import com.sample.feature.logger.logs.repository.db.LogDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
class LogLocalDataSource @Inject constructor(
    private val logDao: LogDao
) : LogDataSource {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    override suspend fun addLog(log: Log) {
        withContext(ioDispatcher) {
            logDao.insert(log)
        }
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        logDao.getAll().asLiveData().observeForever {
            callback(it)
        }
    }

    override suspend fun removeLogs() {
        withContext(ioDispatcher) {
            logDao.deleteAll()
        }
    }

    override fun getAllLogs(): Flow<List<Log>> {
        return logDao.getAll()
    }


}
