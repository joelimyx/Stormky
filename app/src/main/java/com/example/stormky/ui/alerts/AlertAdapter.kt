package com.example.stormky.ui.alerts

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stormky.databinding.ItemAlertBinding
import com.example.stormky.model.Alert

class AlertAdapter : ListAdapter<Alert, AlertAdapter.AlertViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<Alert>() {
        override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem.start == newItem.start
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem == newItem
        }

    }

    class AlertViewHolder(private var binding: ItemAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(unit: Alert) {
            binding.apply {
                alertLayout.setOnClickListener {
                    if (unit.collapse) {
                        descriptionText.apply {
                            maxLines = 100
                            ellipsize = null
                        }
                    }else{
                        descriptionText.apply {
                            maxLines = 3
                            ellipsize = TextUtils.TruncateAt.END
                        }
                    }
                    unit.collapse = !unit.collapse
                }
                alert = unit
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
            ItemAlertBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}