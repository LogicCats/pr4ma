package com.example.pr4ma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlantViewModel(private val plantDao: PlantDao) : ViewModel() {



    fun addPlant(plant: Plant) {
        viewModelScope.launch {
            plantDao.insertPlant(plant)

        }
    }



    fun getPlants() {
        viewModelScope.launch {
            val plants = plantDao.getAllPlants()

        }
    }
}
