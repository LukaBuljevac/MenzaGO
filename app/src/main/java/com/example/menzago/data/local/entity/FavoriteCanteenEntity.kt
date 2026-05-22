package com.example.menzago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_canteens")
data class FavoriteCanteenEntity(
    @PrimaryKey
    val canteenId: Int
)