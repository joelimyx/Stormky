package com.example.stormky.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*


data class Forecast (val current:Current)//, val hourly: Hourly, val daily: Daily, val alerts: Alerts)

class Alerts (

)

class Daily {

}

class Hourly {

}

data class Current (
    @Json(name="dt")val currentTime:Long,
    val temp:Double,
    @Json(name = "feels_like") val feelsLike:Double

    )

fun getFormattedTime(time:Long): String = SimpleDateFormat("h:m a", Locale.getDefault()).format(Date(time*1000))
