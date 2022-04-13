package com.example.stormky

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.stormky.model.ForecastViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class FetchDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val outputData = Data.Builder()
            .putString("first", "Hi Da")
            .build()

        val size = inputData.getInt(alertKey,0).toString()
        Timber.i(size)
        val builder = NotificationCompat.Builder(applicationContext, ForecastApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_error_outline_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle("Weather alert")
            .setContentText(inputData.getString(alertTitle))
            .setStyle(NotificationCompat.BigTextStyle().bigText(inputData.getString(alertText)))

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }

        return Result.success(outputData)
    }
    companion object{
        const val alertKey = "alertSize"
        const val alertTitle = "alertTitle"
        const val alertText = "alertText"
    }
}

