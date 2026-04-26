package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.repository.MenzaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CanteensUiData(
    val canteens: List<Canteen>,
    val searchQuery: String = ""
)

class CanteensViewModel : ViewModel() {

    private val repository = MenzaRepository

    private val _uiState = MutableStateFlow(
        CanteensUiData(
            canteens = repository.canteens.value
        )
    )

    val uiState: StateFlow<CanteensUiData> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.value = CanteensUiData(
            canteens = repository.searchCanteens(query),
            searchQuery = query
        )
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        repository.toggleCanteenFavorite(canteenId)

        _uiState.value = _uiState.value.copy(
            canteens = repository.searchCanteens(_uiState.value.searchQuery)
        )
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(
            canteens = repository.searchCanteens(_uiState.value.searchQuery)
        )
    }
}