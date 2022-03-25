package com.example.stormky.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stormky.network.ForecastApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class WeatherViewModel : ViewModel() {

    private val _weather = MutableLiveData<Weather>()
    val weather:LiveData<Weather> = _weather

    init {
        getAllWeather(40.74, 73.98)
    }
    fun getAllWeather(lat:Double, lon:Double){
        viewModelScope.launch {
            try {
                _weather.value = ForecastApi.retrofitService.getAllWeather(lat, lon, units = "imperial")
            }catch (e: Exception){
                throw IllegalArgumentException("Wrong coord or api")
            }
        }
    }
}