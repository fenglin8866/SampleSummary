package com.sample.data.repository.book

data class Book(
    val id: Long,
    val title: String,
    val description: String,
    val timestamp: Long,
    val isBookmarked: Boolean
)
