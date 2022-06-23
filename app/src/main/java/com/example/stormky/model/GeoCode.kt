package com.example.stormky.model

data class GeoCode(
    val name: String,
    val state: String?,
    val country: String,
    val lat: Double,
    val lon: Double
)
