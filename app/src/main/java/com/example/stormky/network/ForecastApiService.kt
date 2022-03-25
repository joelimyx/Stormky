package com.example.stormky.network

import com.example.stormky.BuildConfig
import com.example.stormky.model.Forecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://api.openweathermap.org/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ForecastApiService {
    @GET("data/2.5/onecall")
    suspend fun getForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon: Double,
        @Query("appid") appid:String = BuildConfig.KEY,
        @Query("units") units:String,
        @Query("exclude") exclude:String = "minutely"
    ): Forecast
}

object ForecastApi{
    val retrofitService: ForecastApiService by lazy {
        retrofit.create(ForecastApiService::class.java)
    }
}