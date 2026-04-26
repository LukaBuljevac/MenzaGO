package com.example.menzago.data.model

data class Canteen(
    val id: Int,
    val name: String,
    val location: String,
    val distanceMeters: Int,
    val isOpen: Boolean,
    val workingHours: String,
    val todaysOffer: List<String>,
    val isFavorite: Boolean = false
)