package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiData(
    val nearestCanteen: Canteen?,
    val todayDishes: List<Dish>,
    val previewCanteens: List<Canteen>,
    val searchQuery: String = ""
)

class HomeViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<HomeUiData> = _uiState.asStateFlow()

    private fun createInitialState(): HomeUiData {
        val canteens = repository.getAllCanteens()
        val dishes = repository.getAllDishes()

        return HomeUiData(
            nearestCanteen = canteens.minByOrNull { it.distanceMeters },
            todayDishes = dishes.take(3),
            previewCanteens = canteens.take(2)
        )
    }

    fun onSearchQueryChange(query: String) {
        val filteredDishes = repository.getAllDishes().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            todayDishes = filteredDishes.take(3)
        )
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