package com.example.menzago.data.repository

import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MenzaRepository {

    private val _dishes = MutableStateFlow(MockData.dishes)
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    private val _canteens = MutableStateFlow(MockData.canteens)
    val canteens: StateFlow<List<Canteen>> = _canteens.asStateFlow()

    fun getDishById(id: Int): Dish? {
        return _dishes.value.firstOrNull { it.id == id }
    }

    fun getCanteenById(id: Int): Canteen? {
        return _canteens.value.firstOrNull { it.id == id }
    }

    fun getFavoriteDishes(): List<Dish> {
        return _dishes.value.filter { it.isFavorite }
    }

    fun getFavoriteCanteens(): List<Canteen> {
        return _canteens.value.filter { it.isFavorite }
    }

    fun toggleDishFavorite(dishId: Int) {
        _dishes.value = _dishes.value.map { dish ->
            if (dish.id == dishId) {
                dish.copy(isFavorite = !dish.isFavorite)
            } else {
                dish
            }
        }
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        _canteens.value = _canteens.value.map { canteen ->
            if (canteen.id == canteenId) {
                canteen.copy(isFavorite = !canteen.isFavorite)
            } else {
                canteen
            }
        }
    }

    fun searchDishes(query: String): List<Dish> {
        if (query.isBlank()) return _dishes.value

        return _dishes.value.filter { dish ->
            dish.name.contains(query, ignoreCase = true) ||
                    dish.description.contains(query, ignoreCase = true) ||
                    dish.category.contains(query, ignoreCase = true)
        }
    }

    fun searchCanteens(query: String): List<Canteen> {
        if (query.isBlank()) return _canteens.value

        return _canteens.value.filter { canteen ->
            canteen.name.contains(query, ignoreCase = true) ||
                    canteen.location.contains(query, ignoreCase = true)
        }
    }
}