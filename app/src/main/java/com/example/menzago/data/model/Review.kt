package com.example.menzago.data.model

data class Review(
    val id: String = "",
    val dishId: Int = 0,
    val userEmail: String = "",
    val comment: String = "",
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)