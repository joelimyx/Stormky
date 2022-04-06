package com.example.stormky.ui.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stormky.R
import com.example.stormky.databinding.ItemHourlyBinding
import com.example.stormky.model.Current

class HourlyAdapter:ListAdapter<Current, HourlyAdapter.HourlyViewHolder>(DiffCallback) {

    private companion object DiffCallback: DiffUtil.ItemCallback<Current>(){
        override fun areItemsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem.currentTime == newItem.currentTime
        }

        override fun areContentsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem == newItem
        }

    }
    class HourlyViewHolder(private var binding: ItemHourlyBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(current: Current, position: Int){
                binding.hourly = current
                val day = position/24
                val hours = position % 24
                val hoursText:String
                when(position){
                    in 1..23-> hoursText = "$hours H"
                    24 -> hoursText = "$day D"
                    else -> hoursText = "$day D $hours H"
                }
                binding.hourText.text = hoursText
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(ItemHourlyBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val current = getItem(position)
        AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_insert_anim)
        holder.bind(current, position+1)

    }
}