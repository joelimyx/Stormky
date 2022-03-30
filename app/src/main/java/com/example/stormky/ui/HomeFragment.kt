package com.example.stormky.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.R
import com.example.stormky.databinding.FragmentHomeBinding
import com.example.stormky.model.ForecastViewModel
import com.example.stormky.model.getFormattedTime
import com.google.android.material.elevation.SurfaceColors

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

        forecastViewModel.current.observe(viewLifecycleOwner){
            timeTextView.text = getString(R.string.current_time, getFormattedTime(it.currentTime))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = forecastViewModel
            lifecycleOwner = viewLifecycleOwner
            homeFragment = this@HomeFragment
        }
        val color = SurfaceColors.SURFACE_0.getColor(requireContext())


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getWeatherInFrag(lat:Double, lon:Double){
        forecastViewModel.getWeatherByLoc(lat, lon)


    }
}