package com.example.stormky.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [WeatherEnt::class],
    version = 3
)

abstract class AppDatabase:RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                //DB Migration
                val migration1_2 = object : Migration(1,2){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE `weathert` (`id` INTEGER, `lat` DOUBLE, `long` DOUBLE, PRIMARY KEY(`id`))")
                        database.execSQL("DROP TABLE `weather`")
                        database.execSQL("ALTER TABLE weathert RENAME TO weather")
                    }
                }
                val migration2_3 = object: Migration(2,3){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE weather ADD COLUMN  type TEXT")
                    }
                }

                val instance = Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_database")
                    .addMigrations(migration1_2, migration2_3)
                    .createFromAsset("database/weather.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}