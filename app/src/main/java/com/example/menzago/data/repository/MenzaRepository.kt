package com.example.menzago.data.repository

import com.example.menzago.data.local.dao.FavoritesDao
import com.example.menzago.data.local.entity.FavoriteCanteenEntity
import com.example.menzago.data.local.entity.FavoriteDishEntity
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MenzaRepository(
    private val dao: FavoritesDao
) {

    fun getAllDishes(): List<Dish> {
        return MockData.dishes
    }

    fun getAllCanteens(): List<Canteen> {
        return MockData.canteens
    }

    fun favoriteDishIds(): Flow<List<FavoriteDishEntity>> {
        return dao.getFavoriteDishes()
    }

    fun favoriteCanteenIds(): Flow<List<FavoriteCanteenEntity>> {
        return dao.getFavoriteCanteens()
    }

    suspend fun toggleDishFavorite(dishId: Int) {
        val favorites = dao.getFavoriteDishes().first()

        val exists = favorites.any { it.dishId == dishId }

        if (exists) {
            dao.deleteFavoriteDish(dishId)
        } else {
            dao.insertFavoriteDish(FavoriteDishEntity(dishId))
        }
    }

    suspend fun toggleCanteenFavorite(canteenId: Int) {
        val favorites = dao.getFavoriteCanteens().first()

        val exists = favorites.any { it.canteenId == canteenId }

        if (exists) {
            dao.deleteFavoriteCanteen(canteenId)
        } else {
            dao.insertFavoriteCanteen(FavoriteCanteenEntity(canteenId))
        }
    }
}