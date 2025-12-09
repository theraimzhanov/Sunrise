package com.example.sunrise.screens.main

import androidx.lifecycle.ViewModel
import com.example.sunrise.data.DataOrException
import com.example.sunrise.model.Weather
import com.example.sunrise.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun getWeatherData(city: String, units: String): DataOrException<Weather, Boolean, Exception>{
        return repository.getWeather(city,units=units)
    }
}