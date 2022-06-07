package com.example.stormky.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [WeatherDB::class],
    version = 2
)

abstract class AppDatabase:RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_database")
                    .createFromAsset("database/weather.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}