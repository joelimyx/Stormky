package com.example.stormky.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stormky.model.Weather
import com.example.stormky.network.WeatherApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel : ViewModel() {

    private val _weather = MutableLiveData<Weather>()
    val weather:LiveData<Weather> = _weather

    init {
        getSunRise(40.74, 73.98)
    }
    private fun getSunRise(lat:Double, lon:Double){
        viewModelScope.launch {
            try {
                _weather.value = WeatherApi.retrofitService.getAllWeather(lat, lon)
                Log.i("weatherViewModel", getFormattedTime(weather.value!!.current.sunrise))
            }catch (e: Exception){
                throw IllegalArgumentException("Wrong coord or api")
            }
        }
    }
    fun getFormattedTime(time:Long): String = SimpleDateFormat("h:m a", Locale.getDefault()).format(Date(time*1000))
}