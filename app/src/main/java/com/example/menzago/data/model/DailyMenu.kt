package com.example.menzago.data.model

data class DailyMenu(
    val id: String = "",
    val canteenId: Int = 0,
    val date: String = "",
    val dishIds: List<Int> = emptyList()
)