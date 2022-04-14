package com.example.stormky.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

data class Forecast(
    val current: Current,
    @Json(name = "hourly") val hourlyList: List<Current>?,
    @Json(name = "daily") val dailyList: List<Daily>?,
    @Json(name = "alerts") val alertList: List<Alert>?
)

data class Current(
    @Json(name = "dt") val currentTime: Long,
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val pop: Double?,
    val rain: Rain?,
    val snow: Snow?,
    val weather: List<Weather>,
    val rainAndSnow: Double =
        (rain?.last_hour ?: 0.0) + (snow?.last_hour ?: 0.0)
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Rain(
    @Json(name = "1h") val last_hour: Double
)

data class Snow(
    @Json(name = "1h") val last_hour: Double
)


data class Daily(
    @Json(name = "dt") val currentTime: Long,
    val temp: Temp,
    val weather: List<Weather>,
    val humidity: Int,
    val wind_speed: Double,
    val pop: Double,
    val rain: Double?,
    val snow: Double?,
    val rainAndSnow: Double =
        (rain ?: 0.0) + (snow ?: 0.0)
)

class Temp(val min: Double, val max: Double)

class Alert(
    val event: String,
    val start: Long,
    val description: String,
    var expand: Boolean = false
) {
    fun toggleExpand() {
        expand = !expand
    }
}

//Helper Functions
fun getFormattedTime(time: Long): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(time * 1000))

fun getFormattedDay(time: Long): String =
    SimpleDateFormat("E", Locale.getDefault()).format(Date(time * 1000)).substring(0..2)

fun meterToMile(meter: Int): Double = meter.toDouble() / 1609

/*
TODO: Try adapter later
class RainAndSnowAdapter{
    @FromJson
    fun fromJson(rainAndSnowJson: RainAndSnowJson): RainAndSnow{

        val rain = (rainAndSnowJson.rain?.last_hour) ?: 0.0
        val snow = (rainAndSnowJson.snow?.last_hour) ?: 0.0
        return RainAndSnow(
            rainAndSnow = rain+snow
        )
    }
    @ToJson
    fun toJson(rainAndSnow: Double): RainAndSnowJson{
        return RainAndSnowJson(
            rain = Rain(0.0),
            snow = Snow(0.0)
        )
    }
}

data class RainAndSnow (
    val rainAndSnow: Double
)

data class RainAndSnowJson (
    val rain: Rain?,
    val snow: Snow?
)
*/