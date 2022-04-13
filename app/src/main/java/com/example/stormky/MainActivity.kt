package com.example.stormky

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.stormky.databinding.ActivityMainBinding
import com.example.stormky.model.ForecastViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val forecastViewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        DynamicColors.applyToActivitiesIfAvailable(application)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_hourly, R.id.navigation_alerts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        startWork()

        forecastViewModel.alertList.observe(this) {
            val alertSize = it.size.toString()
            val builder =
                NotificationCompat.Builder(applicationContext, ForecastApplication.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_error_outline_24)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle(alertSize)

            Timber.i(alertSize)
            if (alertSize.toInt() > 0) {
                with(NotificationManagerCompat.from(applicationContext)) {
                    //notify(2, builder.build())
                }
            }
        }
    }

    fun startWork(){
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val data = Data.Builder()
            .putInt("alertSize", forecastViewModel.alertList.value?.size ?: 0)
            .build()

        val work = PeriodicWorkRequestBuilder<FetchDataWorker>(3, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        forecastViewModel.alertList.observe(this) {
            if (it.isNotEmpty()) {
                val observeData = Data.Builder()
                    .putInt(FetchDataWorker.alertKey, it.size)
                    .putString(FetchDataWorker.alertTitle, it[0].event)
                    .putString(FetchDataWorker.alertTitle, it[0].event)
                    .putString(FetchDataWorker.alertText, it[0].description)
                    .build()
                val oneTime = OneTimeWorkRequestBuilder<FetchDataWorker>()
                    .setConstraints(constraints)
                    .setInputData(observeData)
                    .setInitialDelay(20, TimeUnit.SECONDS)
                    .build()

                Timber.i("Alert size: ${it.size}")

                workManager.enqueue(oneTime)
            }
        }
//        workManager
//            .enqueue(work)
            //.enqueueUniquePeriodicWork("Unique Periodic", ExistingPeriodicWorkPolicy.REPLACE,work)
    }
}
