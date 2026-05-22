package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Comment
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    fun getDishById(dishId: Int): Dish {
        return repository.getAllDishes().firstOrNull { it.id == dishId }
            ?: repository.getAllDishes().first()
    }

    fun getCanteenById(canteenId: Int): Canteen {
        return repository.getAllCanteens().firstOrNull { it.id == canteenId }
            ?: repository.getAllCanteens().first()
    }

    fun getAllDishes(): List<Dish> {
        return repository.getAllDishes()
    }

    fun getComments(): List<Comment> {
        return MockData.comments
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