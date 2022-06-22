package com.example.stormky.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.databinding.FragmentAlertsBinding
import com.example.stormky.model.ForecastViewModel
import timber.log.Timber

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val forecastViewModel: ForecastViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = forecastViewModel
            recyclerView.adapter = AlertAdapter()
        }
        forecastViewModel.alertStatus.observe(viewLifecycleOwner){
            Timber.i("${it}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}