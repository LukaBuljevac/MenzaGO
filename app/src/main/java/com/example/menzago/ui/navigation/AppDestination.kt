package com.example.menzago.ui.navigation

sealed class AppDestination(val route: String) {
    data object Home : AppDestination("home")
    data object Canteens : AppDestination("canteens")
    data object Favorites : AppDestination("favorites")
    data object Profile : AppDestination("profile")
    data object CanteenDetail : AppDestination("canteen_detail/{canteenId}") {
        fun createRoute(canteenId: Int) = "canteen_detail/$canteenId"
    }
    data object DishDetail : AppDestination("dish_detail/{dishId}") {
        fun createRoute(dishId: Int) = "dish_detail/$dishId"
    }
}