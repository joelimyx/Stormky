package com.example.stormky.model

import androidx.lifecycle.*
import com.example.stormky.database.WeatherDao
import com.example.stormky.database.WeatherEnt
import com.example.stormky.network.ForecastApi
import kotlinx.coroutines.launch

enum class AlertStatus{EMPTY, DONE}

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

    //Hourly/Daily
    private val _hourlyList = MutableLiveData<List<Current>>()
    val hourlyList: LiveData<List<Current>> = _hourlyList

    private val _dailyList = MutableLiveData<List<Daily>>()
    val dailyList: LiveData<List<Daily>> = _dailyList

    private val _listSwitch = MutableLiveData(true)
    val listSwitch: LiveData<Boolean> = _listSwitch

    //Alert
    private val _alertList = MutableLiveData(listOf<Alert>())
    val alertList: LiveData<List<Alert>> = _alertList

    private val _alertStatus = MutableLiveData<AlertStatus>()
    val alertStatus: LiveData<AlertStatus> = _alertStatus

    //Location
    private val _geocode = MutableLiveData<GeoCode>()
    val geoCode: LiveData<GeoCode> = _geocode

    //Database
    val dbData: LiveData<List<WeatherEnt>> = weatherDao.getAll().asLiveData()

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
                    _alertStatus.value = AlertStatus.DONE
                }else{
                    _alertStatus.value = AlertStatus.EMPTY
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getCityByLoc(lat: Double, lon: Double){
        viewModelScope.launch {
            try {
                _geocode.value = ForecastApi.retrofitService.getCityByCoord(lat, lon)[0]
            }catch (e: Exception){
                throw e
            }

        }
    }

    fun getCityByZip(zip: String){
        viewModelScope.launch {
            try {
                _geocode.value = ForecastApi.retrofitService.getCityByZip("${zip},US")
                getWeatherByLoc(geoCode.value!!.lat, geoCode.value!!.lon)
            }catch (e: Exception){
                throw e
            }
        }
    }

    fun toggleSwitch() {
        _listSwitch.value = !_listSwitch.value!!
    }

    //Database
    fun addLocation(lat:Double, lon: Double, type: String){
        viewModelScope.launch {
            weatherDao.insertLoc(WeatherEnt(id = 1, lat=lat, lon = lon, type = type))
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