package com.example.stormky.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat


data class Weather (val current:Current)//, val hourly: Hourly, val daily: Daily, val alerts: Alerts)

class Alerts (

)

class Daily {

}

class Hourly {

}

data class Current (
    @Json(name="dt")val currentTime:Long,
    val sunrise:Long,
    val sunset:Long)
