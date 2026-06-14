package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val email: String = "",
    val displayName: String = "",
    val errorMessage: String? = null
)

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(
        AuthUiState(
            isLoggedIn = authRepository.isUserLoggedIn(),
            email = authRepository.currentUser?.email.orEmpty(),
            displayName = authRepository.currentUser?.displayName.orEmpty()
        )
    )

    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = authRepository.login(email, password)

            _uiState.value = if (result.isSuccess) {
                val user = result.getOrNull()

                AuthUiState(
                    isLoggedIn = true,
                    isLoading = false,
                    email = user?.email.orEmpty(),
                    displayName = user?.displayName.orEmpty()
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Prijava nije uspjela."
                )
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Unesi ime korisnika."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = authRepository.register(
                name = name.trim(),
                email = email,
                password = password
            )

            _uiState.value = if (result.isSuccess) {
                val user = result.getOrNull()

                AuthUiState(
                    isLoggedIn = true,
                    isLoading = false,
                    email = user?.email.orEmpty(),
                    displayName = user?.displayName.orEmpty()
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Registracija nije uspjela."
                )
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    fun getCurrentEmail(): String {
        return authRepository.currentUser?.email.orEmpty()
    }

    fun getCurrentDisplayName(): String {
        return authRepository.currentUser?.displayName
            ?.takeIf { it.isNotBlank() }
            ?: "Student korisnik"
    }
}