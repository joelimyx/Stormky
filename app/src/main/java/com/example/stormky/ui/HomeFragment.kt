package com.example.stormky.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.R
import com.example.stormky.databinding.FragmentHomeBinding
import com.example.stormky.model.ForecastViewModel
import com.example.stormky.model.getFormattedTime
import com.google.android.gms.location.LocationServices
import timber.log.Timber


@SuppressLint("MissingPermission")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val forecastViewModel: ForecastViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val timeTextView: TextView = binding.timeText

        forecastViewModel.current.observe(viewLifecycleOwner) {
            timeTextView.text = getString(R.string.current_time, getFormattedTime(it.currentTime))
            val uvPercent = 780*(it.uvi).div(10)
            Timber.i("UV: ${it.uvi} Percent: $uvPercent")
            val animation = TranslateAnimation(0F, (uvPercent).toFloat(), 0F, 0F)
            animation.duration = 2000
            animation.fillAfter = true
            binding.uvArrowIcon.startAnimation(animation)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val uvPercent = (forecastViewModel.current.value?.uvi)!!.div(10)
        val animation = TranslateAnimation(0F, 780F, 0F, 0F)
        animation.duration = 2000
        animation.fillAfter = true

        _binding?.apply {
            viewModel = forecastViewModel
            lifecycleOwner = viewLifecycleOwner
            homeFragment = this@HomeFragment

//            uvArrowIcon.startAnimation(animation)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getWeatherInFrag() {

        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
            forecastViewModel.getWeatherByLoc(it.latitude, it.longitude)
        }
    }
}