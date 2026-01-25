package com.sample.feature.logger.logs.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sample.feature.logger.logs.repository.Log
import kotlinx.coroutines.flow.Flow

/**
 * Data access object to query the database.
 */
@Dao
interface LogDao {

    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun getAll(): Flow<List<Log>>

    @Insert
    suspend fun insert(logs: Log)

    @Query("DELETE FROM logs")
    suspend fun deleteAll()

}