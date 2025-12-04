package com.example.sunrise.network

import retrofit2.http.Query
import com.example.sunrise.model.Weather
import com.example.sunrise.utils.Constants.API_KEY
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface WeatherApi {
    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appid: String = API_KEY
    ): Weather
}