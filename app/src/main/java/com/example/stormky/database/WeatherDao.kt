package com.example.stormky.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherdb")
    fun getAll(): Flow<List<WeatherDB>>

    @Insert
    suspend fun insertLoc(weatherDB: WeatherDB)
}