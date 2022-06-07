package com.example.stormky.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherDB (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val lat: Double,
    val lon: Double
)