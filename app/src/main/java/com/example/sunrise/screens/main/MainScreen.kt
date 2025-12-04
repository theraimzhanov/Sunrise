package com.example.sunrise.screens.main


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sunrise.data.DataOrException
import com.example.sunrise.model.Weather

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel= hiltViewModel()) {

    ShowData(mainViewModel)
}

@Composable
fun ShowData(mainViewModel: MainViewModel) {
    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)) {
        value = mainViewModel.getWeatherData("London")
    }.value

    if (weatherData.loading ==true){
        CircularProgressIndicator()
    } else if (weatherData.data !=null){
        Text(weatherData.data!!.toString())
    }

}