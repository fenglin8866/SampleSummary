/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.sample.feature.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.util.timestampToReadableDate
import com.sample.data.repository.book.Book
import com.sample.data.repository.book.BookRepository
import com.sample.feature.book.list.ListUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    /**
     * SharingStarted.WhileSubscribed(5000),
     * 1、有订阅者时才开始收集
     * 2、当最后一个订阅者取消后，会等待5秒，如果这段时间内没有新的订阅者，就停止收集
     */
    val uiState: StateFlow<ListUiState> = bookRepository
        .observeAllBooks
        .map<List<Book>, ListUiState> { items ->
            items.map { book ->
                BookUiState(
                    id = book.id,
                    title = book.title,
                    date = timestampToReadableDate(book.timestamp),
                    isBookmarked = book.isBookmarked
                )
            }.let(::Success)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ListUiState.Loading
        )

    fun bookmark(id: Long, isBookmarked: Boolean) {
        viewModelScope.launch {
            bookRepository.bookmark(id, isBookmarked)
        }
    }
}
