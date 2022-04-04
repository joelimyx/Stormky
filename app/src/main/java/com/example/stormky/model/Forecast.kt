package com.example.stormky.model

import androidx.annotation.Nullable
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*


data class Forecast (val current:Current, @Json(name="hourly")val hourlyList: List<Current>)//, val daily: Daily, val alerts: Alerts)

data class Current (
    @Json(name="dt")val currentTime: Long,
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double,
    val clouds:Int,
    val visibility:Int,
    val wind_speed:Double,
    val pop:Double?,
    val rain:Rain?,
    val snow:Snow?,
    val weather: List<Weather>
    )

class Weather(
    val id:Int,
    val main: String,
    val description: String,
    val icon: String
    )

class Daily {

}

class Rain(
    @Json(name = "1h")val last_hour: Double
)

class Snow(
    @Json(name = "1h")val last_hour: Double
)

class Alerts (

)

fun getFormattedTime(time:Long): String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(time*1000))

fun meterToMile(meter:Int):Double = meter.toDouble()/1609
