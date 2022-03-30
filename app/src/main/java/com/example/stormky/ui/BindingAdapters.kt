package com.example.stormky.ui

import android.media.Image
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.stormky.R

class BindingAdapters {
}

@BindingAdapter("iconUrl")
fun bindImage(imgView:ImageView, iconUrl: String?){
    iconUrl?.let {
        imgView.context.applicationInfo.theme
        val iconUri = "https://openweathermap.org/img/wn/${it.substring(0..1)}d@2x.png"
        imgView.load(iconUri){
            placeholder(R.drawable.ic_baseline_error_outline_24)
        }
    }
}