package com.example.stormky.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAll(): Flow<List<WeatherEnt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoc(weatherEnt: WeatherEnt)

    @Update
    suspend fun updateLoc(weatherEnt: WeatherEnt)
}