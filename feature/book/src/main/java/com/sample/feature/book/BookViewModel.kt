package com.sample.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.repository.book.BookRepository
import com.sample.feature.book.BookUiState.Loading
import com.sample.feature.book.BookUiState.Success
import com.sample.feature.book.BookUiState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(
    private val mBookRepository: BookRepository
) : ViewModel() {

    val uiState: StateFlow<BookUiState> = mBookRepository
        .bookNames.map<List<String>, BookUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addBook(name: String) {
        viewModelScope.launch {
            mBookRepository.add(name)
        }
    }
}

sealed interface BookUiState {
    object Loading : BookUiState
    data class Error(val throwable: Throwable) : BookUiState
    data class Success(val data: List<String>) : BookUiState
}
