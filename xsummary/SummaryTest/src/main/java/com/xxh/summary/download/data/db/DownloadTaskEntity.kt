package com.xxh.summary.download.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_task")
data class DownloadTaskEntity(
    @PrimaryKey val appId: String,
    val status: Int,
    val progress: Int,
    val updateTime: Long
)