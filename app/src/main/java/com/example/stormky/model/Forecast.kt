package com.example.stormky.model

import com.squareup.moshi.*
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
    val rain: Rain?,
    val snow: Snow?,
    val weather: List<Weather>,
    val rainAndSnow:Double =
        (rain?.last_hour ?:0.0) + (snow?.last_hour ?:0.0)
    )

data class Weather(
    val id:Int,
    val main: String,
    val description: String,
    val icon: String
    )

class Daily {

}

data class Rain(
    @Json(name = "1h")val last_hour: Double
)

data class Snow(
    @Json(name = "1h")val last_hour: Double
)

class Alerts (

)

fun getFormattedTime(time:Long): String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(time*1000))

fun meterToMile(meter:Int):Double = meter.toDouble()/1609

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