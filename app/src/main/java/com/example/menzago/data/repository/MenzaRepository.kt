package com.example.menzago.data.repository

import com.example.menzago.data.local.dao.FavoritesDao
import com.example.menzago.data.local.entity.FavoriteCanteenEntity
import com.example.menzago.data.local.entity.FavoriteDishEntity
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import com.example.menzago.data.remote.RemoteMenuService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MenzaRepository(
    private val dao: FavoritesDao
) {
    private val remoteService = RemoteMenuService()

    fun getAllDishes(): List<Dish> = MockData.dishes

    fun getAllCanteens(): List<Canteen> = MockData.canteens

    fun observeDishes(): Flow<List<Dish>> {
        return dao.getFavoriteDishes().map { favoriteEntities ->
            val favoriteIds = favoriteEntities.map { it.dishId }.toSet()

            MockData.dishes.map { dish ->
                dish.copy(isFavorite = dish.id in favoriteIds)
            }
        }
    }

    fun observeCanteens(): Flow<List<Canteen>> {
        return dao.getFavoriteCanteens().map { favoriteEntities ->
            val favoriteIds = favoriteEntities.map { it.canteenId }.toSet()

            MockData.canteens.map { canteen ->
                canteen.copy(isFavorite = canteen.id in favoriteIds)
            }
        }
    }

    fun favoriteDishIds(): Flow<List<FavoriteDishEntity>> {
        return dao.getFavoriteDishes()
    }

    fun favoriteCanteenIds(): Flow<List<FavoriteCanteenEntity>> {
        return dao.getFavoriteCanteens()
    }

    suspend fun getDishById(dishId: Int): Dish {
        val favoriteIds = dao.getFavoriteDishes()
            .first()
            .map { it.dishId }
            .toSet()

        return MockData.dishes.firstOrNull { it.id == dishId }
            ?.copy(isFavorite = dishId in favoriteIds)
            ?: MockData.dishes.first()
    }

    suspend fun getCanteenById(canteenId: Int): Canteen {
        val favoriteIds = dao.getFavoriteCanteens()
            .first()
            .map { it.canteenId }
            .toSet()

        return MockData.canteens.firstOrNull { it.id == canteenId }
            ?.copy(isFavorite = canteenId in favoriteIds)
            ?: MockData.canteens.first()
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

    suspend fun refreshDishesFromRemote(): List<Dish> {
        return remoteService.fetchDishes()
    }

    suspend fun refreshCanteensFromRemote(): List<Canteen> {
        return remoteService.fetchCanteens()
    }
}