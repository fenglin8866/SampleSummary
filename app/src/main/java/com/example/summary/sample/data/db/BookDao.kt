package com.example.summary.sample.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.summary.sample.vo.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("select * from book")
    fun getBooks(): Flow<List<Book>>

    @Query("select * from book where id=:id")
    fun getBooks(id: Int): Flow<Book>

    @Query("select * from book where name=:name")
    fun getBooks(name: String): Flow<List<Book>>

    @Query("select * from book where name in (:names)")
    fun getBooks(vararg names: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun delBook(book: Book)

}