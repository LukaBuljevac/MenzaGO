package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CanteensUiData(
    val canteens: List<Canteen> = emptyList(),
    val searchQuery: String = ""
)

class CanteensViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private var latestCanteens: List<Canteen> = emptyList()

    private val _uiState = MutableStateFlow(CanteensUiData())
    val uiState: StateFlow<CanteensUiData> = _uiState.asStateFlow()

    init {
        observeCanteens()
    }

    private fun observeCanteens() {
        viewModelScope.launch {
            repository.observeCanteens().collect { canteens ->
                latestCanteens = canteens

                _uiState.value = _uiState.value.copy(
                    canteens = filterCanteens(
                        canteens = latestCanteens,
                        query = _uiState.value.searchQuery
                    )
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            canteens = filterCanteens(
                canteens = latestCanteens,
                query = query
            )
        )
    }

    fun toggleCanteenFavorite(canteenId: Int) {
        viewModelScope.launch {
            repository.toggleCanteenFavorite(canteenId)
        }
    }

    fun refresh() {
        onSearchQueryChange(_uiState.value.searchQuery)
    }

    private fun filterCanteens(
        canteens: List<Canteen>,
        query: String
    ): List<Canteen> {
        if (query.isBlank()) return canteens

        return canteens.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.location.contains(query, ignoreCase = true)
        }
    }
}