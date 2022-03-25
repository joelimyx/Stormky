package com.example.stormky.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stormky.R
import com.example.stormky.databinding.FragmentHomeBinding
import com.example.stormky.model.WeatherViewModel
import com.example.stormky.model.getFormattedTime

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val timeTextView: TextView = binding.timeText

        weatherViewModel.current.observe(viewLifecycleOwner){
            timeTextView.text = getString(R.string.current_time, getFormattedTime(it.currentTime))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = weatherViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}