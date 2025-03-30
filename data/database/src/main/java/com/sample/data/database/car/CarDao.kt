package com.sample.data.database.car

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Query("select * from car")
    fun getCars(): Flow<List<Car>>

    @Query("select * from car where id=:id")
    fun getCars(id: Int): Flow<Car>

    @Query("select * from car where name=:name")
    fun getCars(name: String): Flow<List<Car>>

    @Query("select * from car where name in (:names)")
    fun getCars(vararg names: String): Flow<List<Car>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Delete
    suspend fun delCar(car: Car)

}