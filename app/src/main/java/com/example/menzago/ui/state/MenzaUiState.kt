package com.example.menzago.ui.state

sealed interface MenzaUiState<out T> {
    data object Loading : MenzaUiState<Nothing>
    data class Success<T>(val data: T) : MenzaUiState<T>
    data class Error(val message: String) : MenzaUiState<Nothing>
}