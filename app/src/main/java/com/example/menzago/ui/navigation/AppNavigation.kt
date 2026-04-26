package com.example.menzago.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.menzago.ui.components.MenzaGoBottomBar
import com.example.menzago.ui.screens.canteens.CanteenDetailScreen
import com.example.menzago.ui.screens.canteens.CanteensScreen
import com.example.menzago.ui.screens.dish.DishDetailScreen
import com.example.menzago.ui.screens.favorites.FavoritesScreen
import com.example.menzago.ui.screens.home.HomeScreen
import com.example.menzago.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            MenzaGoBottomBar(navController = navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                    onSeeAllCanteens = {
                        navController.navigate(AppDestination.Canteens.route)
                    },
                    onOpenDish = { dishId ->
                        navController.navigate(AppDestination.DishDetail.createRoute(dishId))
                    },
                    onOpenCanteen = { canteenId ->
                        navController.navigate(AppDestination.CanteenDetail.createRoute(canteenId))
                    }
                )
            }

            composable(AppDestination.Canteens.route) {
                CanteensScreen(
                    onOpenCanteen = { canteenId ->
                        navController.navigate(AppDestination.CanteenDetail.createRoute(canteenId))
                    }
                )
            }

            composable(AppDestination.Favorites.route) {
                FavoritesScreen(
                    onOpenDish = { dishId ->
                        navController.navigate(AppDestination.DishDetail.createRoute(dishId))
                    },
                    onOpenCanteen = { canteenId ->
                        navController.navigate(AppDestination.CanteenDetail.createRoute(canteenId))
                    }
                )
            }

            composable(AppDestination.Profile.route) {
                ProfileScreen()
            }

            composable(
                route = AppDestination.CanteenDetail.route,
                arguments = listOf(navArgument("canteenId") { type = NavType.IntType })
            ) { backStackEntry ->
                val canteenId = backStackEntry.arguments?.getInt("canteenId") ?: 1
                CanteenDetailScreen(
                    canteenId = canteenId,
                    onBack = { navController.popBackStack() },
                    onOpenDish = { dishId ->
                        navController.navigate(AppDestination.DishDetail.createRoute(dishId))
                    }
                )
            }

            composable(
                route = AppDestination.DishDetail.route,
                arguments = listOf(navArgument("dishId") { type = NavType.IntType })
            ) { backStackEntry ->
                val dishId = backStackEntry.arguments?.getInt("dishId") ?: 1
                DishDetailScreen(
                    dishId = dishId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}