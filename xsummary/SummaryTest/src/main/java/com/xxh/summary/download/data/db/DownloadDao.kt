package com.xxh.summary.download.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DownloadDao {

    @Query("SELECT * FROM download_task")
    suspend fun getAll(): List<DownloadTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DownloadTaskEntity)

    @Query("DELETE FROM download_task WHERE appId = :appId")
    suspend fun delete(appId: String)
}