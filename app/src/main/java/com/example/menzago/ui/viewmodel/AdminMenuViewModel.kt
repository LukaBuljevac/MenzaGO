package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Dish
import com.example.menzago.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminMenuUiState(
    val message: String? = null,
    val isLoading: Boolean = false
)

class AdminMenuViewModel : ViewModel() {

    private val repository = RepositoryProvider.repository

    private val _uiState = MutableStateFlow(AdminMenuUiState())
    val uiState: StateFlow<AdminMenuUiState> = _uiState.asStateFlow()

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun addOrUpdateDish(
        id: Int?,
        name: String,
        description: String,
        category: String,
        calories: Int?,
        allergens: List<String>,
        imageName: String
    ) {
        if (id == null || id <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan ID jela.")
            return
        }

        if (name.isBlank() || description.isBlank() || category.isBlank()) {
            _uiState.value = AdminMenuUiState(message = "Naziv, opis i kategorija su obavezni.")
            return
        }

        if (calories == null || calories <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan broj kalorija.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminMenuUiState(isLoading = true)

            try {
                repository.addDishToFirestore(
                    Dish(
                        id = id,
                        name = name.trim(),
                        description = description.trim(),
                        category = category.trim(),
                        calories = calories,
                        allergens = allergens,
                        rating = 0.0,
                        imageName = imageName.trim()
                    )
                )

                _uiState.value = AdminMenuUiState(message = "Jelo je uspješno dodano/ažurirano.")
            } catch (e: Exception) {
                _uiState.value = AdminMenuUiState(message = "Greška kod spremanja jela.")
            }
        }
    }

    fun deleteDish(dishId: Int?) {
        if (dishId == null || dishId <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan ID jela za brisanje.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminMenuUiState(isLoading = true)

            try {
                val exists = repository.getAllDishes().any { it.id == dishId }

                if (!exists) {
                    _uiState.value = AdminMenuUiState(message = "Jelo s tim ID-em ne postoji.")
                    return@launch
                }

                repository.deleteDishFromFirestore(dishId)
                _uiState.value = AdminMenuUiState(message = "Jelo je uspješno obrisano.")
            } catch (e: Exception) {
                _uiState.value = AdminMenuUiState(message = "Greška kod brisanja jela.")
            }
        }
    }

    fun removeDishFromDailyMenu(
        canteenId: Int?,
        dishId: Int?
    ) {
        if (canteenId == null || canteenId <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan ID menze.")
            return
        }

        if (dishId == null || dishId <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan ID jela za uklanjanje.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminMenuUiState(isLoading = true)

            try {
                repository.removeDishFromDailyMenu(
                    canteenId = canteenId,
                    dishId = dishId
                )

                _uiState.value = AdminMenuUiState(
                    message = "Jelo je uklonjeno s današnjeg menija."
                )
            } catch (e: Exception) {
                _uiState.value = AdminMenuUiState(
                    message = e.message ?: "Greška kod uklanjanja jela s menija."
                )
            }
        }
    }

    fun saveDailyMenu(
        canteenId: Int?,
        dishIds: List<Int>
    ) {
        if (canteenId == null || canteenId <= 0) {
            _uiState.value = AdminMenuUiState(message = "Unesi ispravan ID menze.")
            return
        }

        if (dishIds.isEmpty()) {
            _uiState.value = AdminMenuUiState(message = "Unesi barem jedan ID jela.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminMenuUiState(isLoading = true)

            try {
                val canteenExists = repository.getAllCanteens().any { it.id == canteenId }

                if (!canteenExists) {
                    _uiState.value = AdminMenuUiState(message = "Menza s tim ID-em ne postoji.")
                    return@launch
                }

                val existingDishIds = repository.getAllDishes().map { it.id }.toSet()
                val invalidDishIds = dishIds.filter { it !in existingDishIds }

                if (invalidDishIds.isNotEmpty()) {
                    _uiState.value = AdminMenuUiState(
                        message = "Ne postoje jela s ID-em: ${invalidDishIds.joinToString()}"
                    )
                    return@launch
                }

                repository.saveDailyMenu(canteenId, dishIds)
                _uiState.value = AdminMenuUiState(message = "Dnevni meni je uspješno spremljen.")
            } catch (e: Exception) {
                _uiState.value = AdminMenuUiState(message = "Greška kod spremanja dnevnog menija.")
            }
        }
    }
}