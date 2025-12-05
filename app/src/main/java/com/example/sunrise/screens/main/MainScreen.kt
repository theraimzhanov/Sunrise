package com.example.sunrise.screens.main


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sunrise.R
import com.example.sunrise.data.DataOrException
import com.example.sunrise.model.Weather
import com.example.sunrise.widgets.WeatherAppBar

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel= hiltViewModel()) {

    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)) {
        value = mainViewModel.getWeatherData("London")
    }.value

    if (weatherData.loading ==true){
        CircularProgressIndicator()
    } else if (weatherData.data !=null){
        MainScaffold(weatherData.data!!, navController)
    }

}

@Composable
fun MainScaffold(weather: Weather,navController: NavController) {
    Scaffold(topBar = { WeatherAppBar(weather.city.name+", ${weather.city.country}"
        ,navController = navController, elevation = 1.dp){

    }}){
        MainContent(data = weather)
    }

}


@Composable
fun MainContent(data: Weather){
Text(data.list.toString())
}