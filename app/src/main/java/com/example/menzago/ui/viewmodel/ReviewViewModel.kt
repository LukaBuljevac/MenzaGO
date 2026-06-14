package com.example.menzago.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menzago.data.model.Review
import com.example.menzago.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val reviews: List<Review> = emptyList(),
    val averageRating: Double = 0.0,
    val reviewCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ReviewViewModel : ViewModel() {

    private val repository = ReviewRepository()

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun loadReviews(dishId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val reviews = repository.getReviewsForDish(dishId)

                val average = if (reviews.isNotEmpty()) {
                    reviews.map { it.rating }.average()
                } else {
                    0.0
                }

                _uiState.value = ReviewUiState(
                    reviews = reviews,
                    averageRating = average,
                    reviewCount = reviews.size,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Komentari se trenutno ne mogu učitati."
                )
            }
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            try {
                repository.addReview(review)
                loadReviews(review.dishId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Komentar nije spremljen. Pokušaj ponovno."
                )
            }
        }
    }

    fun deleteReview(
        reviewId: String,
        dishId: Int
    ) {
        viewModelScope.launch {
            try {
                repository.deleteReview(reviewId)
                loadReviews(dishId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Komentar nije obrisan."
                )
            }
        }
    }
}