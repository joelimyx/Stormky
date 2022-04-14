package com.example.stormky

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.stormky.network.ForecastApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class FetchDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        try {
            val lat = inputData.getDouble(latKey, 0.0)
            val lon = inputData.getDouble(lonKey, 0.0)
            val alerts =
                ForecastApi.retrofitService.getAlertsOnly(lat, lon, units = "imperial").alertList
            Timber.i(alerts?.size.toString())

            val outputData = Data.Builder()
                .putString("first", "Hi Da")
                .build()

            val alertPendingIntent =
                NavDeepLinkBuilder(applicationContext)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.navigation_alerts)
                    .createPendingIntent()

            val builder =
                NotificationCompat.Builder(applicationContext, ForecastApplication.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_error_outline_24)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle("Weather alert")
                    .setContentIntent(alertPendingIntent)

            if (!alerts.isNullOrEmpty()) {
                val inbox = NotificationCompat.InboxStyle()
                alerts.forEach {
                    inbox.addLine(it.event)
                }
                builder.setStyle(inbox)
            }
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1, builder.build())
            }

            return Result.success(outputData)
        } catch (e: Error) {
            val errorData = Data.Builder()
                .putString("Error", e.toString())
                .build()
            return Result.failure(errorData)
        }
    }

    companion object {
        const val alertKey = "alertSize"
        const val alertTitle = "alertTitle"
        const val alertText = "alertText"

        const val latKey = "lat"
        const val lonKey = "lon"
    }
}

