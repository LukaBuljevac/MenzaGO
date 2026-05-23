package com.example.menzago.data.remote

import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Dish
import kotlinx.coroutines.delay

class RemoteMenuService {

    suspend fun fetchDishes(): List<Dish> {
        delay(800)
        return MockData.dishes
    }

    suspend fun fetchCanteens(): List<Canteen> {
        delay(600)
        return MockData.canteens
    }
}