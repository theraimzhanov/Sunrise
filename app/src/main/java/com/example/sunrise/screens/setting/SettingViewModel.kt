package com.example.sunrise.screens.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunrise.model.Unit
import com.example.sunrise.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: WeatherDbRepository) : ViewModel() {
    private val _unitList = MutableStateFlow<List<Unit>>(emptyList())
    val unitList = _unitList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUnits().distinctUntilChanged()
                .collect {list->
                    if (list.isNullOrEmpty()){
                       // _unitList.value = emptyList()
                    } else{
                        _unitList.value = list
                    }
                }
        }
    }

    fun insertUnit(unit: Unit) = viewModelScope.launch { repository.insertUnit(unit) }
    fun updateUnit(unit: Unit) = viewModelScope.launch { repository.updateUnit(unit) }
    fun deleteAllUnits() = viewModelScope.launch { repository.deleteAllUnits() }
    fun deleteUnit(unit: Unit) = viewModelScope.launch { repository.deleteUnit(unit) }
}