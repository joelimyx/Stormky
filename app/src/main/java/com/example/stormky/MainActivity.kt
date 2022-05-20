package com.example.stormky

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.stormky.databinding.ActivityMainBinding
import com.example.stormky.model.ForecastViewModel
import com.example.stormky.ui.hourly.HourlyFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        DynamicColors.applyToActivitiesIfAvailable(application)
        setContentView(binding.root)

        bottomNavView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView.setupWithNavController(navController)

    }

    override fun onStart() {
        super.onStart()

        Timber.i("onStart: ${intent?.extras.toString()}")

        bottomNavView.setOnItemReselectedListener {
            when(it.itemId){
                R.id.navigation_hourly->{
                    val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                    val hourFrag = navHost?.childFragmentManager?.fragments?.get(0) as HourlyFragment
                    hourFrag.scrollToTop()
                }
            }
        }
        startWork()
    }

    override fun onResume() {
        super.onResume()
        viewModel.alertList.observe(this){
            bottomNavView.getOrCreateBadge(R.id.navigation_alerts).isVisible = it.isNotEmpty()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intentToHourly(intent)
    }

    fun intentToHourly(intent: Intent?){
        if (intent?.extras != null) {
            Timber.i((intent.extras.toString()))
            navController.navigate(R.id.action_widget_to_hourly)
        }
    }

    fun startWork() {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val alertData = Data.Builder()
            .putDouble(FetchDataWorker.latKey, 35.08)
            .putDouble(FetchDataWorker.lonKey, -106.11)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<FetchDataWorker>(2, TimeUnit.SECONDS)
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

    fun getLocation(){
        val locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            when{
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ->{
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        it.apply {
                            Timber.i(longitude.toString())
                            Timber.i(latitude.toString())
                        }
                    }
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        it.apply {
                            Timber.i(longitude.toString())
                            Timber.i(latitude.toString())
                        }
                    }
                }else ->{
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        when (PackageManager.PERMISSION_GRANTED) {
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION), checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    it.apply {
                        Timber.i(longitude.toString())
                        Timber.i(latitude.toString())
                    }
                }
            }
            else -> {
                locationPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun mockLocation(){

        fusedLocationProviderClient.setMockMode(true)
        val mockLocation = Location("Mock")
        mockLocation.latitude = 39.46
        mockLocation.longitude = -119.78
        fusedLocationProviderClient.setMockLocation(mockLocation)
    }

}