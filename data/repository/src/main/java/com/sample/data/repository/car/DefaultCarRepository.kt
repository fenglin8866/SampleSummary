package com.sample.data.repository.car

import com.sample.data.database.car.Car
import com.sample.data.database.car.CarDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultCarRepository @Inject constructor(private val carDao: CarDao) : CarRepository {

    fun getBookForName(name: String) = carDao.getCars(name)

    fun getBookForId(id: Int) = carDao.getCars(id)

    fun getBooks() = carDao.getCars()

    suspend fun addBook(book: Car) {
        carDao.insertCar(book)
    }

    override val carNames: Flow<List<String>>
        get() = carDao.getCars().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        carDao.insertCar(Car(name = name))
    }

}