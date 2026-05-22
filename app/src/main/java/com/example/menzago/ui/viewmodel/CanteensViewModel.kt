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
    val canteens: List<Canteen>,
    val searchQuery: String = ""
)

class CanteensViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private val _uiState = MutableStateFlow(
        CanteensUiData(canteens = repository.getAllCanteens())
    )

    val uiState: StateFlow<CanteensUiData> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        val filtered = repository.getAllCanteens().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.location.contains(query, ignoreCase = true)
        }

        _uiState.value = CanteensUiData(
            canteens = filtered,
            searchQuery = query
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
}