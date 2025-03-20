package com.example.summary.sample.data

import com.example.summary.sample.data.db.BookDao
import com.example.summary.sample.vo.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(private val bookDao: BookDao) {

    fun getBookForName(name: String) = bookDao.getBooks(name)

    fun getBookForId(id: Int) = bookDao.getBooks(id)

    fun getBooks()=bookDao.getBooks()

    suspend fun addBook(book: Book){
        bookDao.insertBook(book)
    }

}