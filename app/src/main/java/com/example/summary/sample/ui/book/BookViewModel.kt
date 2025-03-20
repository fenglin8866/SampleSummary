package com.example.summary.sample.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.summary.sample.data.BookRepository
import com.example.summary.sample.vo.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    val books: LiveData<List<Book>> = repository.getBooks().asLiveData()

    fun getBookForId(id: Int): LiveData<Book> {
        return repository.getBookForId(id).asLiveData()
    }

    fun getBookForName(name: String): LiveData<List<Book>> {
        return repository.getBookForName(name).asLiveData()
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.addBook(book)
        }
    }

}