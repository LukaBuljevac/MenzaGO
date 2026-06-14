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

data class HomeUiData(
    val nearestCanteen: Canteen? = null,
    val todayDishes: List<Dish> = emptyList(),
    val previewCanteens: List<Canteen> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private val _uiState = MutableStateFlow(HomeUiData(isLoading = true))
    val uiState: StateFlow<HomeUiData> = _uiState.asStateFlow()

    init {
        observeHomeData()
        refreshMenu()
        loadFirestoreDishes()
    }

    private fun loadFirestoreDishes() {
        viewModelScope.launch {
            repository.loadDishesFromFirestore()
        }
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            combine(
                repository.observeDishes(),
                repository.observeCanteens()
            ) { dishes, canteens ->
                val current = _uiState.value
                val filteredDishes = filterDishes(dishes, current.searchQuery)

                current.copy(
                    nearestCanteen = canteens.minByOrNull { it.distanceMeters },
                    todayDishes = filteredDishes.take(3),
                    previewCanteens = canteens.take(2)
                )
            }.collect { data ->
                _uiState.value = data
            }
        }
    }

    fun refreshMenu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                repository.refreshDishesFromRemote()
                repository.refreshCanteensFromRemote()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Neuspjelo osvježavanje menija."
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val filtered = filterDishes(
            dishes = repository.getAllDishes(),
            query = query
        )

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            todayDishes = filtered.take(3)
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

    private fun filterDishes(
        dishes: List<Dish>,
        query: String
    ): List<Dish> {
        if (query.isBlank()) return dishes

        return dishes.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }
}