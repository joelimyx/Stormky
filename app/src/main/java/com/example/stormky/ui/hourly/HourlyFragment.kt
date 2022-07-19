package com.example.stormky.ui.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.R
import com.example.stormky.databinding.FragmentHourlyBinding
import com.example.stormky.model.ForecastViewModel

class HourlyFragment : Fragment() {

    private var _binding: FragmentHourlyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val forecastViewModel: ForecastViewModel by activityViewModels()
    private var isWideHourly: Boolean = false
    private var isWideDaily: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isWideHourly = arguments?.getBoolean(getString(R.string.is_wide_hourly)) == true
        isWideDaily = arguments?.getBoolean(getString(R.string.is_wide_daily)) == true

        _binding = FragmentHourlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = forecastViewModel
            hourlyFragment = this@HourlyFragment
            recyclerView.adapter = HourlyAdapter()
            dailyRecyclerView.adapter = DailyAdapter()
        }
        if(isWideDaily){
            binding.recyclerView.visibility = View.INVISIBLE
            binding.dailyRecyclerView.visibility = View.VISIBLE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun switchList() {
        //Switch to Daily
        if (forecastViewModel.listSwitch.value == true) {
            forecastViewModel.toggleSwitch()
            binding.recyclerView.visibility = View.INVISIBLE
            binding.dailyRecyclerView.visibility = View.VISIBLE
        } else {
        //Switch to Hourly
            forecastViewModel.toggleSwitch()
            binding.dailyRecyclerView.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    fun scrollToTop() {
        if (forecastViewModel.listSwitch.value == true || isWideHourly) {
            binding.recyclerView.scrollToPosition(0)
        } else {
            binding.dailyRecyclerView.scrollToPosition(0)
        }
    }
}