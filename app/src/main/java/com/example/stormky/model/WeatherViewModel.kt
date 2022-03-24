package com.example.stormky.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stormky.model.Weather
import com.example.stormky.network.WeatherApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

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
            }catch (e: Exception){
                throw IllegalArgumentException("Wrong coord or api")
            }
        }
    }

}