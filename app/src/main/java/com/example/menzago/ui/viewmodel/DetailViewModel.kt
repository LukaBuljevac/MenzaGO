package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Comment
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.MenzaRepository

class DetailViewModel : ViewModel() {

    private val repository = MenzaRepository

    fun getDishById(dishId: Int): Dish {
        return repository.getDishById(dishId) ?: repository.dishes.value.first()
    }

    fun getCanteenById(canteenId: Int): Canteen {
        return repository.getCanteenById(canteenId) ?: repository.canteens.value.first()
    }

    fun getAllDishes(): List<Dish> {
        return repository.dishes.value
    }

    fun getComments(): List<Comment> {
        return MockData.comments
    }

    fun toggleDishFavorite(dishId: Int) {
        repository.toggleDishFavorite(dishId)
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        repository.toggleCanteenFavorite(canteenId)
    }
}