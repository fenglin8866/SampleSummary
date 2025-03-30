package com.sample.data.repository.di

import com.sample.data.repository.book.BookRepository
import com.sample.data.repository.book.DefaultBookRepository
import com.sample.data.repository.car.CarRepository
import com.sample.data.repository.car.DefaultCarRepository
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
    fun bindsCarRepository(repository: DefaultCarRepository): CarRepository

    @Singleton
    @Binds
    fun bindsBookRepository(repository: DefaultBookRepository): BookRepository
}