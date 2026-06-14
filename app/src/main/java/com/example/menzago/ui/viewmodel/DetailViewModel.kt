package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    fun getAllDishes(): List<Dish> {
        return repository.getAllDishes()
    }

    fun getDishById(dishId: Int): Dish {
        return runBlocking {
            repository.getDishById(dishId)
        }
    }

    fun getCanteenById(canteenId: Int): Canteen {
        return runBlocking {
            repository.getCanteenById(canteenId)
        }
    }

    suspend fun getTodaysDishesForCanteen(canteenId: Int): List<Dish> {
        return repository.getTodaysDishesForCanteen(canteenId)
    }

    fun toggleDishFavorite(dishId: Int) {
        viewModelScope.launch {
            repository.toggleDishFavorite(dishId)
        }
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        viewModelScope.launch {
            repository.toggleCanteenFavorite(canteenId)
        }
    }
}