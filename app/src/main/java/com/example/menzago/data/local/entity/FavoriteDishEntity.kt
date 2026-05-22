package com.example.menzago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_dishes")
data class FavoriteDishEntity(
    @PrimaryKey
    val dishId: Int
)