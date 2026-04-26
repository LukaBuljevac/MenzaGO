package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.MenzaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FavoritesUiData(
    val favoriteDishes: List<Dish>,
    val favoriteCanteens: List<Canteen>
)

class FavoritesViewModel : ViewModel() {

    private val repository = MenzaRepository

    private val _uiState = MutableStateFlow(
        FavoritesUiData(
            favoriteDishes = repository.getFavoriteDishes(),
            favoriteCanteens = repository.getFavoriteCanteens()
        )
    )

    val uiState: StateFlow<FavoritesUiData> = _uiState.asStateFlow()

    fun refreshFavorites() {
        _uiState.value = FavoritesUiData(
            favoriteDishes = repository.getFavoriteDishes(),
            favoriteCanteens = repository.getFavoriteCanteens()
        )
    }

    fun toggleDishFavorite(dishId: Int) {
        repository.toggleDishFavorite(dishId)
        refreshFavorites()
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        repository.toggleCanteenFavorite(canteenId)
        refreshFavorites()
    }
}