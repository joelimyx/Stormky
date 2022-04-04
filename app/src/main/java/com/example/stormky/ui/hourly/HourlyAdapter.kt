package com.example.stormky.ui.hourly

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stormky.databinding.HourlyItemBinding
import com.example.stormky.model.Current
import com.example.stormky.model.getFormattedTime

class HourlyAdapter:ListAdapter<Current, HourlyAdapter.HourlyViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Current>(){
        override fun areItemsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem.currentTime == newItem.currentTime
        }

        override fun areContentsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem == newItem
        }

    }
    class HourlyViewHolder(private var binding: HourlyItemBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(current: Current){
                binding.hourly = current
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(HourlyItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}