package com.example.stormky.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.ForecastApplication
import com.example.stormky.R
import com.example.stormky.databinding.FragmentHomeBinding
import com.example.stormky.model.ForecastViewModel
import com.example.stormky.model.ForecastViewModelFactory
import com.example.stormky.model.getFormattedTime
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


@SuppressLint("MissingPermission")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val forecastViewModel: ForecastViewModel by activityViewModels{
        ForecastViewModelFactory(
            (activity?.application as ForecastApplication).database.weatherDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = forecastViewModel
            lifecycleOwner = viewLifecycleOwner
            homeFragment = this@HomeFragment
        }
    }

    override fun onStart() {
        super.onStart()

        forecastViewModel.current.observe(viewLifecycleOwner) {
            binding.timeText.text = getString(R.string.current_time, getFormattedTime(it.currentTime))

            val uvPercent = binding.uvScaleImage.width*(it.uvi).div(10)

            val animation = TranslateAnimation(0F, (uvPercent).toFloat(), 0F, 0F)
            animation.duration = 2000
            animation.fillAfter = true
            binding.uvArrowIcon.startAnimation(animation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getWeatherInFrag() {

        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
            forecastViewModel.getWeatherByLoc(it.latitude, it.longitude)

            Toast.makeText(requireContext(), "Location Updated", Toast.LENGTH_SHORT)
                .show()
        }
    }
}