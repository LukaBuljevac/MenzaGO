package com.example.menzago.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.menzago.data.local.dao.FavoritesDao
import com.example.menzago.data.local.entity.FavoriteCanteenEntity
import com.example.menzago.data.local.entity.FavoriteDishEntity

@Database(
    entities = [
        FavoriteDishEntity::class,
        FavoriteCanteenEntity::class
    ],
    version = 1
)
abstract class MenzaDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
}