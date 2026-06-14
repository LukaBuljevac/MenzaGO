package com.example.menzago.data.repository

import android.location.Location
import com.example.menzago.data.local.dao.FavoritesDao
import com.example.menzago.data.local.entity.FavoriteCanteenEntity
import com.example.menzago.data.local.entity.FavoriteDishEntity
import com.example.menzago.data.location.DistanceUtils
import com.example.menzago.data.location.UserLocationRepository
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.DailyMenu
import com.example.menzago.data.model.Dish
import com.example.menzago.data.remote.RemoteMenuService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import com.example.menzago.data.utils.CanteenStatusUtils

class MenzaRepository(
    private val dao: FavoritesDao
) {
    private val remoteService = RemoteMenuService()
    private val menuAdminRepository = MenuAdminRepository()
    private val firestoreDishes = MutableStateFlow(MockData.dishes)

    fun getAllDishes(): List<Dish> = firestoreDishes.value

    fun getAllCanteens(): List<Canteen> {
        return applyUserDistances(MockData.canteens, UserLocationRepository.location.value)
    }

    fun observeDishes(): Flow<List<Dish>> {
        return combine(
            dao.getFavoriteDishes(),
            firestoreDishes
        ) { favoriteEntities, dishes ->
            val favoriteIds = favoriteEntities.map { it.dishId }.toSet()

            dishes.map { dish ->
                dish.copy(isFavorite = dish.id in favoriteIds)
            }
        }
    }

    fun observeCanteens(): Flow<List<Canteen>> {
        return combine(
            dao.getFavoriteCanteens(),
            UserLocationRepository.location
        ) { favoriteEntities, userLocation ->
            val favoriteIds = favoriteEntities.map { it.canteenId }.toSet()

            applyUserDistances(MockData.canteens, userLocation)
                .map { canteen ->
                    canteen.copy(isFavorite = canteen.id in favoriteIds)
                }
                .sortedBy { it.distanceMeters }
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

        return firestoreDishes.value
            .firstOrNull { it.id == dishId }
            ?.copy(isFavorite = dishId in favoriteIds)
            ?: firestoreDishes.value.first()
    }

    suspend fun getCanteenById(canteenId: Int): Canteen {
        val favoriteIds = dao.getFavoriteCanteens()
            .first()
            .map { it.canteenId }
            .toSet()

        return getAllCanteens()
            .firstOrNull { it.id == canteenId }
            ?.copy(isFavorite = canteenId in favoriteIds)
            ?: getAllCanteens().first()
    }

    suspend fun removeDishFromDailyMenu(
        canteenId: Int,
        dishId: Int
    ) {
        val today = LocalDate.now().toString()

        val currentMenu = menuAdminRepository.getDailyMenu(
            canteenId = canteenId,
            date = today
        )

        if (currentMenu == null) {
            throw IllegalStateException("Dnevni meni za ovu menzu ne postoji.")
        }

        if (dishId !in currentMenu.dishIds) {
            throw IllegalStateException("To jelo nije na današnjem meniju.")
        }

        val updatedDishIds = currentMenu.dishIds.filter { it != dishId }

        menuAdminRepository.saveDailyMenu(
            currentMenu.copy(
                dishIds = updatedDishIds
            )
        )
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

    suspend fun loadDishesFromFirestore() {
        val dishes = menuAdminRepository.getDishes()

        firestoreDishes.value = if (dishes.isNotEmpty()) {
            dishes
        } else {
            MockData.dishes
        }
    }

    suspend fun addDishToFirestore(dish: Dish) {
        menuAdminRepository.addOrUpdateDish(dish)
        loadDishesFromFirestore()
    }

    suspend fun deleteDishFromFirestore(dishId: Int) {
        menuAdminRepository.deleteDish(dishId)
        loadDishesFromFirestore()
    }

    suspend fun saveDailyMenu(
        canteenId: Int,
        dishIds: List<Int>
    ) {
        val today = LocalDate.now().toString()

        menuAdminRepository.saveDailyMenu(
            DailyMenu(
                canteenId = canteenId,
                date = today,
                dishIds = dishIds
            )
        )
    }

    suspend fun getTodaysDishesForCanteen(canteenId: Int): List<Dish> {
        loadDishesFromFirestore()

        val today = LocalDate.now().toString()

        val menu = menuAdminRepository.getDailyMenu(
            canteenId = canteenId,
            date = today
        )

        if (menu == null || menu.dishIds.isEmpty()) {
            return firestoreDishes.value
        }

        return firestoreDishes.value.filter { dish ->
            dish.id in menu.dishIds
        }
    }

    private fun applyUserDistances(
        canteens: List<Canteen>,
        userLocation: Location?
    ): List<Canteen> {
        return canteens.map { canteen ->

            val realDistance = if (userLocation != null) {
                DistanceUtils.distanceToCanteenMeters(
                    userLocation = userLocation,
                    canteen = canteen
                ).toInt()
            } else {
                canteen.distanceMeters
            }

            canteen.copy(
                distanceMeters = realDistance,
                isOpen = CanteenStatusUtils.isOpenNow(canteen.workingHours)
            )
        }
    }
}