package com.example.stormky.model

import androidx.lifecycle.*
import com.example.stormky.network.ForecastApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class ForecastViewModel : ViewModel() {

    private val _forecast = MutableLiveData<Forecast>()
    private val forecast:LiveData<Forecast> = _forecast

    private val _current = MutableLiveData<Current>()
    val current:LiveData<Current> = _current

    private val _visibility = MutableLiveData<Int>()
    val visibility:LiveData<Double> = Transformations.map(_visibility){
        meterToMile(it)
    }

    private val _weather = MutableLiveData<List<Weather>>()
    val weather:LiveData<List<Weather>> = _weather

    init {
        getWeatherByLoc(40.74, 73.98)
    }
    fun getWeatherByLoc(lat:Double, lon:Double){
        viewModelScope.launch {
            try {
                _forecast.value = ForecastApi.retrofitService.getForecast(lat, lon, units = "imperial")
                _current.value = forecast.value!!.current
                _visibility.value = current.value!!.visibility
                _weather.value = current.value!!.weather
            }catch (e: Exception){
                throw e
            }
        }
    }
}