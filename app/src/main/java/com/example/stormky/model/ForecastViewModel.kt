package com.example.stormky.model

import androidx.lifecycle.*
import com.example.stormky.database.WeatherDB
import com.example.stormky.database.WeatherDao
import com.example.stormky.network.ForecastApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException

class ForecastViewModel(private val weatherDao: WeatherDao) : ViewModel() {

    private val _forecast = MutableLiveData<Forecast>()
    private val forecast: LiveData<Forecast> = _forecast

    private val _current = MutableLiveData<Current>()
    val current: LiveData<Current> = _current

    private val _visibility = MutableLiveData<Int>()
    val visibility: LiveData<Double> = Transformations.map(_visibility) {
        meterToMile(it)
    }

    private val _weather = MutableLiveData<List<Weather>>()
    val weather: LiveData<List<Weather>> = _weather

    private val _hourlyList = MutableLiveData<List<Current>>()
    val hourlyList: LiveData<List<Current>> = _hourlyList

    private val _dailyList = MutableLiveData<List<Daily>>()
    val dailyList: LiveData<List<Daily>> = _dailyList

    private val _listSwitch = MutableLiveData(true)
    val listSwitch: LiveData<Boolean> = _listSwitch

    private val _alertList = MutableLiveData(listOf<Alert>())
    val alertList: LiveData<List<Alert>> = _alertList

    private val _alertSize = MutableLiveData(0)
    val alertSize: LiveData<Int> = _alertSize

    val dbData: LiveData<List<WeatherDB>> = weatherDao.getAll().asLiveData()

    init {
        getWeatherByLoc(47.57, -101.93)
    }

    fun getWeatherByLoc(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _forecast.value =
                    ForecastApi.retrofitService.getForecast(lat, lon, units = "imperial")

                //Current
                _current.value = forecast.value!!.current
                _visibility.value = current.value!!.visibility
                _weather.value = current.value!!.weather

                //Hourly
                _hourlyList.value = forecast.value!!.hourlyList!!

                //daily
                _dailyList.value = forecast.value!!.dailyList!!

                if (forecast.value!!.alertList != null) {
                    _alertList.value = forecast.value!!.alertList!!
                }else{
                    _alertList.value = listOf()
                }
                _alertSize.value = alertList.value!!.size

            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun toggleSwitch() {
        _listSwitch.value = !_listSwitch.value!!
    }

    fun addLocation(lat:Double, lon: Double){
        viewModelScope.launch {
            weatherDao.insertLoc(WeatherDB(lat=lat, lon = lon))
        }
    }
}

class ForecastViewModelFactory(private val weatherDao: WeatherDao):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(weatherDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}