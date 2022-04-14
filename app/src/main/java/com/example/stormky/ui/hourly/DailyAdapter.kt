package com.example.stormky.ui.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stormky.databinding.ItemDailyBinding
import com.example.stormky.model.Daily
import com.example.stormky.model.getFormattedDay

class DailyAdapter : ListAdapter<Daily, DailyAdapter.DailyViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.currentTime == newItem.currentTime
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }

    }

    class DailyViewHolder(private var binding: ItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: Daily) {
            binding.apply {
                this.daily = daily
                dayText.text = getFormattedDay(daily.currentTime)
                executePendingBindings()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            ItemDailyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}