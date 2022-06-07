package com.example.stormky

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Configuration
import com.example.stormky.database.AppDatabase
import com.example.stormky.database.WeatherDB
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ForecastApplication : Application(), Configuration.Provider {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        val name = "Special Weather alert"
        val descriptionText = "Notification for when there is a special weather alerts."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        //DB Migration
        val migration1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `weathert` (`id` INTEGER, `lat` DOUBLE, `long` DOUBLE, PRIMARY KEY(`id`))")
                database.execSQL("DROP TABLE `weather`")
                database.execSQL("ALTER TABLE weathert RENAME TO weather")
            }
        }
        Room.databaseBuilder(this, AppDatabase::class.java, "weather.db")
            .fallbackToDestructiveMigration()
            .addMigrations(migration1_2)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "weather_alert_id"
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}