package com.example.menzago.data.repository

import com.example.menzago.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addReview(review: Review) {
        val existingReview = firestore.collection("reviews")
            .whereEqualTo("dishId", review.dishId)
            .whereEqualTo("userEmail", review.userEmail)
            .get()
            .await()

        if (!existingReview.isEmpty) {
            throw IllegalStateException("Već si objavio recenziju za ovo jelo.")
        }

        firestore.collection("reviews")
            .add(review)
            .await()
    }

    suspend fun getReviewsForDish(
        dishId: Int
    ): List<Review> {
        return firestore.collection("reviews")
            .whereEqualTo("dishId", dishId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(Review::class.java)
                    ?.copy(id = document.id)
            }
    }

    suspend fun deleteReview(reviewId: String) {
        firestore.collection("reviews")
            .document(reviewId)
            .delete()
            .await()
    }
}