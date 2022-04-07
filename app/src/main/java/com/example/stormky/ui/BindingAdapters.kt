package com.example.stormky.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.stormky.R
import com.example.stormky.model.Current
import com.example.stormky.model.Daily
import com.example.stormky.ui.hourly.DailyAdapter
import com.example.stormky.ui.hourly.HourlyAdapter


@BindingAdapter("iconUrl")
fun bindImage(imgView: ImageView, iconUrl: String?) {
    iconUrl?.let {
        imgView.context.applicationInfo.theme
        val iconUri = "https://openweathermap.org/img/wn/${it.substring(0..1)}d@2x.png"
        imgView.load(iconUri) {
            placeholder(R.drawable.ic_baseline_error_outline_24)
        }
    }
}

@BindingAdapter("listData")
fun bindHourlyRecyclerView(recyclerView: RecyclerView, data: List<Current>) {
    val adapter = recyclerView.adapter as HourlyAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDaily")
fun bindDailyRecyclerView(recyclerView: RecyclerView, data: List<Daily>) {
    val adapter = recyclerView.adapter as DailyAdapter
    adapter.submitList(data)
}
