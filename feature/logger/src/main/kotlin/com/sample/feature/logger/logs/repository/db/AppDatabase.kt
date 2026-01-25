package com.sample.feature.logger.logs.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.feature.logger.logs.repository.Log
/**
 * SQLite Database for storing the logs.
 */
@Database(entities = [Log::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "logging.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}