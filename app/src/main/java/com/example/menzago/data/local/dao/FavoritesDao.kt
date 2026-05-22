package com.example.menzago.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.menzago.data.local.entity.FavoriteCanteenEntity
import com.example.menzago.data.local.entity.FavoriteDishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite_dishes")
    fun getFavoriteDishes(): Flow<List<FavoriteDishEntity>>

    @Query("SELECT * FROM favorite_canteens")
    fun getFavoriteCanteens(): Flow<List<FavoriteCanteenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteDish(entity: FavoriteDishEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCanteen(entity: FavoriteCanteenEntity)

    @Query("DELETE FROM favorite_dishes WHERE dishId = :dishId")
    suspend fun deleteFavoriteDish(dishId: Int)

    @Query("DELETE FROM favorite_canteens WHERE canteenId = :canteenId")
    suspend fun deleteFavoriteCanteen(canteenId: Int)
}