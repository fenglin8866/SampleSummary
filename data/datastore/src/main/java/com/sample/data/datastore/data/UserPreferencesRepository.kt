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

package com.sample.data.datastore.data

import android.util.Log
import androidx.datastore.core.DataStore
import com.sample.data.datastore.proto.UserPreferences
import com.sample.data.datastore.proto.UserPreferences.SortOrder
import com.sample.data.datastore.proto.copy
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<UserPreferences>) {

    private val TAG: String = "UserPreferencesRepo"

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                //emit(null)
            } else {
                throw exception
            }
        }

    /**
     * Enable / disable sort by deadline.
     */
    suspend fun enableSortByDeadline(enable: Boolean) {
        // updateData handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_PRIORITY) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_DEADLINE
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_PRIORITY
                    } else {
                        SortOrder.NONE
                    }
                }
            Log.i("xxh1234", "enableSortByDeadline enable=$enable sortOrder=${newSortOrder.name}")
            preferences.copy {
                sortOrder = newSortOrder
            }
           // preferences.toBuilder().setSortOrder(newSortOrder).build()
        }
    }

    /**
     * Enable / disable sort by priority.
     */
    suspend fun enableSortByPriority(enable: Boolean) {
        // updateData handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
            Log.i("xxh1234", "enableSortByPriority enable=$enable sortOrder=${newSortOrder.name}")
            preferences.copy {
                sortOrder = newSortOrder
            }
        }
    }

    suspend fun updateShowCompleted(isShowCompleted: Boolean) {
        dataStore.updateData { preferences ->
            preferences.copy {
                showCompleted = isShowCompleted
            }
        }
    }
}