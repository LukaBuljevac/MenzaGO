package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class FavoritesUiData(
    val favoriteDishes: List<Dish> = emptyList(),
    val favoriteCanteens: List<Canteen> = emptyList()
)

class FavoritesViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private val _uiState = MutableStateFlow(FavoritesUiData())
    val uiState: StateFlow<FavoritesUiData> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            combine(
                repository.favoriteDishIds(),
                repository.favoriteCanteenIds()
            ) { dishEntities, canteenEntities ->
                val favoriteDishIds = dishEntities.map { it.dishId }.toSet()
                val favoriteCanteenIds = canteenEntities.map { it.canteenId }.toSet()

                FavoritesUiData(
                    favoriteDishes = repository.getAllDishes().filter { it.id in favoriteDishIds },
                    favoriteCanteens = repository.getAllCanteens().filter { it.id in favoriteCanteenIds }
                )
            }.collect { data ->
                _uiState.value = data
            }
        }
    }

    fun refreshFavorites() {
        // Room Flow automatski osvježava favorite.
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