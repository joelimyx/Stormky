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

    private val _forecast = MutableLiveData<Forecast>()
    val forecast:LiveData<Forecast> = _forecast

    private val _current = MutableLiveData<Current>()
    val current:LiveData<Current> = _current

    init {
        getWeatherByLoc(40.74, 73.98)
    }
    fun getWeatherByLoc(lat:Double, lon:Double){
        viewModelScope.launch {
            try {
                _forecast.value = ForecastApi.retrofitService.getForecast(lat, lon, units = "imperial")
                _current.value = forecast.value?.current
            }catch (e: Exception){
                throw IllegalArgumentException("Wrong coord or api")
            }
        }
    }
}