package com.sample.data.database.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val bookId: Int = 0,
    @ColumnInfo(name = "name")
    val bookName: String
)