package com.example.stormky.ui.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.databinding.FragmentHourlyBinding
import com.example.stormky.model.ForecastViewModel

class HourlyFragment : Fragment() {

    private var _binding: FragmentHourlyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val forecastViewModel: ForecastViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
//            dailyRecyclerView.adapter = DailyAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /**
    fun switchList() {
    if (forecastViewModel.listSwitch.value == true) {
    forecastViewModel.toggleSwitch()
    binding.recyclerView.visibility = View.INVISIBLE
    binding.dailyRecyclerView.visibility = View.VISIBLE
    } else {
    forecastViewModel.toggleSwitch()
    binding.dailyRecyclerView.visibility = View.INVISIBLE
    binding.recyclerView.visibility = View.VISIBLE
    }
    }
     **/
}