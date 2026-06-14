package com.example.menzago.data.model

data class Dish(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val calories: Int = 0,
    val allergens: List<String> = emptyList(),
    val rating: Double = 0.0,
    val imageName: String = "",
    val isFavorite: Boolean = false
)