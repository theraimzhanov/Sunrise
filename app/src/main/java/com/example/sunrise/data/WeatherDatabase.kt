package com.example.sunrise.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sunrise.model.Favorite
import com.example.sunrise.model.Unit

@Database(entities = [Favorite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}
