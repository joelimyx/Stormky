package com.example.stormky.model

import androidx.lifecycle.*
import com.example.stormky.network.ForecastApi
import kotlinx.coroutines.launch
import java.lang.Exception

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

    private val _rainAndSnow = MutableLiveData<Double>()
    val rainAndSnow:LiveData<Double> = _rainAndSnow

    private val _hourlyList = MutableLiveData<List<Current>>()
    val hourlyList:LiveData<List<Current>> = _hourlyList

    init {
        getWeatherByLoc(40.74, 73.98)
    }
    fun getWeatherByLoc(lat:Double, lon:Double){
        viewModelScope.launch {
            try {
                _forecast.value = ForecastApi.retrofitService.getForecast(lat, lon, units = "imperial")

                //Current
                _current.value = forecast.value!!.current
                _visibility.value = current.value!!.visibility
                _weather.value = current.value!!.weather
                val rain = (current.value!!.rain?.last_hour) ?: 0.0
                val snow = (current.value!!.snow?.last_hour) ?: 0.0
                _rainAndSnow.value = rain+snow

                //Hourly
                _hourlyList.value = forecast.value!!.hourlyList
            }catch (e: Exception){
                throw e
            }
        }
    }
}