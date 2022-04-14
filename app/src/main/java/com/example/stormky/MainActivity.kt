package com.example.stormky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.stormky.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

    }

    fun startWork() {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val alertData = Data.Builder()
            .putDouble(FetchDataWorker.latKey, 40.39)
            .putDouble(FetchDataWorker.lonKey, -98.87)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<FetchDataWorker>(30, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(alertData)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "Unique Periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork
            )
    }
}
