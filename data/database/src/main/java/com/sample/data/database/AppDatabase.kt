package com.sample.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.data.database.car.Car
import com.sample.data.database.car.CarDao

@Database(entities = [Car::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): CarDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}