package com.example.sunrise.repository

import android.util.Log
import com.example.sunrise.data.DataOrException
import com.example.sunrise.model.Weather
import com.example.sunrise.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getWeather(cityQuery: String): DataOrException<Weather, Boolean, Exception> {
        val respond = try {
            api.getWeather(query = cityQuery)
        } catch (e: Exception) {
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $respond")
        return DataOrException(respond)
    }
}