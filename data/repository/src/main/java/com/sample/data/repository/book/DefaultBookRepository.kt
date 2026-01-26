package com.sample.data.repository.book

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultBookRepository @Inject constructor() : BookRepository {
    override val observeAllBooks: Flow<List<Book>>
        get() = allItems

    override fun observeBookById(id: Long): Flow<Book> =observeAllBooks.map { items ->
        items.firstOrNull { book -> book.id == id }
            ?: throw NoSuchElementException("$id not found")
    }

    override suspend fun bookmark(id: Long, isBookmarked: Boolean) {
        allItems.getAndUpdate { items ->
            items.map { book ->
                if(book.id == id) {
                    book.copy(isBookmarked = isBookmarked)
                } else {
                    book
                }
            }
        }
    }

    private val allItems: MutableStateFlow<List<Book>> = MutableStateFlow(
        listOf(
            Book(
                id = 1,
                title = "Compose",
                description = "Description",
                timestamp = 1672368617954,
                isBookmarked = false
            ),
            Book(
                id = 2,
                title = "Lifecycle",
                description = "Description",
                timestamp = 1664678230741,
                isBookmarked = false
            ),
            Book(
                id = 3,
                title = "Logger",
                description = "Description",
                timestamp = 1664679230742,
                isBookmarked = false
            ),
            Book(
                id = 4,
                title = "Navigation",
                description = "Description",
                timestamp = 1667884312189,
                isBookmarked = false
            )
        )
    )

}