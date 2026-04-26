package com.example.menzago.data.model

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val calories: Int,
    val allergens: List<String>,
    val rating: Double,
    val isFavorite: Boolean = false
)