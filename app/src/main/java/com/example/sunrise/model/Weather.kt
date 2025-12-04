package com.example.sunrise.model

data class Weather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Double
)