package com.sample.feature.logger.logs.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represent the a table in the database.
 */
@Entity(tableName = "logs")
data class Log(
    val msg: String,
    val startTime: Long,
    val endTime: Long,
    val startTimeStr: String,
    val endTimeStr: String,
    val duration: Long = endTime - startTime,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}