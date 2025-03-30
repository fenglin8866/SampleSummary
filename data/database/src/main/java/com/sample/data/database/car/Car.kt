package com.sample.data.database.car

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)