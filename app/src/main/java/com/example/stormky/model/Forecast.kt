package com.example.stormky.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*


data class Forecast (val current:Current)//, val hourly: Hourly, val daily: Daily, val alerts: Alerts)

data class Current (
    @Json(name="dt")val currentTime: Long,
    val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double
//    val humidity: Int,
//    val uvi: Double,
//    val clouds:Int,
//    val visibility:Int,
//    @Json(name = "wind_speed") val windSpeed:Int
)

class Weather(
    val id:Int,
    val main: String,
    val description: String,
    val icon: String)

class Daily {

}

class Hourly {

}

class Alerts (

)

fun getFormattedTime(time:Long): String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(time*1000))
