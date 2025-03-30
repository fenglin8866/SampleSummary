package com.sample.data.repository.di

import com.sample.data.repository.book.BookRepository
import com.sample.data.repository.book.DefaultBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsBookRepository(repository: DefaultBookRepository): BookRepository
}