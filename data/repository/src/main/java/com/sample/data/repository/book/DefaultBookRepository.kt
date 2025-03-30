package com.sample.data.repository.book

import com.sample.data.database.book.Book
import com.sample.data.database.book.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultBookRepository @Inject constructor(private val bookDao: BookDao) : BookRepository {

    fun getBookForName(name: String) = bookDao.getBooks(name)

    fun getBookForId(id: Int) = bookDao.getBooks(id)

    fun getBooks() = bookDao.getBooks()

    suspend fun addBook(book: Book) {
        bookDao.insertBook(book)
    }

    override val bookNames: Flow<List<String>>
        get() = bookDao.getBooks().map { items -> items.map { it.bookName } }

    override suspend fun add(name: String) {
        bookDao.insertBook(Book(bookName = name))
    }

}