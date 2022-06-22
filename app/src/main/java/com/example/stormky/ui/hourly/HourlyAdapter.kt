package com.example.stormky.ui.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stormky.databinding.ItemHourlyBinding
import com.example.stormky.model.Current
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter : ListAdapter<Current, HourlyAdapter.HourlyViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<Current>() {
        override fun areItemsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem.currentTime == newItem.currentTime
        }

        override fun areContentsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem == newItem
        }

    }

    class HourlyViewHolder(private var binding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(current: Current) {
            binding.hourly = current

            val temp = SimpleDateFormat("h a", Locale.getDefault()).format(Date(current.currentTime*1000))
            binding.hourText.text = temp

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            ItemHourlyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

    }
}