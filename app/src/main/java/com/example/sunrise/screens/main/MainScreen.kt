package com.example.sunrise.screens.main


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sunrise.data.DataOrException
import com.example.sunrise.model.Item0
import com.example.sunrise.model.Weather
import com.example.sunrise.navigation.WeatherScreens
import com.example.sunrise.screens.favorites.FavoriteViewModel
import com.example.sunrise.utils.formatDate
import com.example.sunrise.utils.formatDecimals
import com.example.sunrise.widgets.HumidityWindPressureRow
import com.example.sunrise.widgets.SunsetSunRiseRow
import com.example.sunrise.widgets.WeatherAppBar
import com.example.sunrise.widgets.WeatherDetailRow
import com.example.sunrise.widgets.WeatherStateImage

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    city: String?
) {
    Log.d("TAG", "MainScreen: $city")
    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.getWeatherData(city.toString())
    }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(weatherData.data!!, navController)
    }

}

@Composable
fun MainScaffold(weather: Weather, navController: NavController) {
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()

    Scaffold(topBar = {
        WeatherAppBar(
            weather.city.name + ", ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {navController.navigate(WeatherScreens.SearchScreen.name)},
            elevation = 1.dp
        ) {

        }
    }) {
        MainContent(data = weather, modifier = Modifier.padding(it))
    }

}


@Composable
fun MainContent(data: Weather, modifier: Modifier) {

    val imageUrl = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}.png"

    Column(
        modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            formatDate(data.list[0].dt),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )

        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = formatDecimals(data.list[0].temp.day) + "°",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = data.list[0].weather[0].main, fontStyle = FontStyle.Italic)
            }
        }
        HumidityWindPressureRow(weather = data.list[0])
        Divider()
        SunsetSunRiseRow(weather = data.list[0])
        Text(
            text = "This Week",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold)
        Surface(modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(14.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                items(items=data.list) { item: Item0->
                  WeatherDetailRow(weather=item)
                }
            }
        }

    }
}

