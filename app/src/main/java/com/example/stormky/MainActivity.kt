package com.example.stormky

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.stormky.databinding.ActivityMainBinding
import com.example.stormky.model.ForecastViewModel
import com.example.stormky.model.ForecastViewModelFactory
import com.example.stormky.ui.hourly.HourlyFragment
import com.example.stormky.ui.widget.StormkyWidget
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.navigationrail.NavigationRailView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navigationRailView: NavigationRailView
    private lateinit var navController: NavController

    var widthDP: Float = 0.0f

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: ForecastViewModel by viewModels {
        ForecastViewModelFactory(
            (application as ForecastApplication).database.weatherDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        WindowCompat.setDecorFitsSystemWindows(window, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        DynamicColors.applyToActivitiesIfAvailable(application)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        widthDP = resources.displayMetrics.widthPixels/resources.displayMetrics.density
        if (widthDP< 600) {
            Timber.i("600px")
            bottomNavView = binding.navView as BottomNavigationView
            bottomNavView.setupWithNavController(navController)
        }else if (widthDP < 840){
            Timber.i("840px")
            navigationRailView = binding.navView as NavigationRailView
            navigationRailView.setupWithNavController(navController)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragmentActivityMain){v, insets ->
            val temp = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = temp.bottom
//                topMargin = temp.top
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onStart() {
        super.onStart()

        if (widthDP < 600) {
            bottomNavView.setOnItemReselectedListener {
                when (it.itemId) {
                    R.id.navigation_hourly -> {
                        val navHost =
                            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                        val hourFrag =
                            navHost?.childFragmentManager?.fragments?.get(0) as HourlyFragment
                        hourFrag.scrollToTop()
                    }
                }
            }
        }else{
            navigationRailView.setOnItemReselectedListener {
                when (it.itemId) {
                    R.id.navigation_hourly -> {
                        val navHost =
                            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                        val hourFrag =
                            navHost?.childFragmentManager?.fragments?.get(0) as HourlyFragment
                        hourFrag.scrollToTop()
                    }
                }
            }
        }

        viewModel.dbData.observe(this) {
//            Timber.i("Size: ${it.size}")
        }

        startWork()
    }

    override fun onResume() {
        super.onResume()
        viewModel.alertList.observe(this) {
            if (widthDP < 600) {
                bottomNavView.getOrCreateBadge(R.id.navigation_alerts).isVisible =
                    !it.isNullOrEmpty()
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Timber.i("${intent?.extras?.getBoolean(StormkyWidget.widgetKey, false)}")

        if (intent?.extras?.getBoolean(FetchDataWorker.notifKey, false) == true) {
            navController.navigate(R.id.action_notif_to_alerts)

        } else if (intent?.extras?.getBoolean(StormkyWidget.widgetKey, false) == true) {
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
            .putDouble(FetchDataWorker.latKey, 47.57)
            .putDouble(FetchDataWorker.lonKey, -101.93)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<FetchDataWorker>(2, TimeUnit.HOURS)
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

    fun getLocation() {
        val locationPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            it.apply {
                                Timber.i("${longitude} register fine")
                                Timber.i(latitude.toString())
                            }
                        }
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            it.apply {
                                Timber.i("${longitude} register course")
                                Timber.i(latitude.toString())
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION), checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    it.apply {
                        if (it != null) {
                            viewModel.addLocation(latitude, longitude, "default")
                            viewModel.getCityByLoc(latitude, longitude)
                        }
                    }
                }
            }
            else -> {
                locationPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun mockLocation() {

        fusedLocationProviderClient.setMockMode(true)
        val mockLocation = Location("Mock")
        mockLocation.latitude = 39.46
        mockLocation.longitude = -119.78
        fusedLocationProviderClient.setMockLocation(mockLocation)
    }

}