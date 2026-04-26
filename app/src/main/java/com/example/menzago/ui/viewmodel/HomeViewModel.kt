package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.MenzaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiData(
    val nearestCanteen: Canteen?,
    val todayDishes: List<Dish>,
    val previewCanteens: List<Canteen>,
    val searchQuery: String = ""
)

class HomeViewModel : ViewModel() {

    private val repository = MenzaRepository

    private val _uiState = MutableStateFlow(
        HomeUiData(
            nearestCanteen = repository.canteens.value.minByOrNull { it.distanceMeters },
            todayDishes = repository.dishes.value.take(3),
            previewCanteens = repository.canteens.value.take(2)
        )
    )

    val uiState: StateFlow<HomeUiData> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        val filteredDishes = repository.searchDishes(query)

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            todayDishes = filteredDishes.take(3)
        )
    }

    fun toggleDishFavorite(dishId: Int) {
        repository.toggleDishFavorite(dishId)
        refresh()
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        repository.toggleCanteenFavorite(canteenId)
        refresh()
    }

    private fun refresh() {
        val query = _uiState.value.searchQuery

        _uiState.value = _uiState.value.copy(
            nearestCanteen = repository.canteens.value.minByOrNull { it.distanceMeters },
            todayDishes = repository.searchDishes(query).take(3),
            previewCanteens = repository.canteens.value.take(2)
        )
    }
}