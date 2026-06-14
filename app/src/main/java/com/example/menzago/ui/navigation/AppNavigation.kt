package com.example.menzago.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.menzago.ui.components.MenzaGoBottomBar
import com.example.menzago.ui.screens.auth.LoginScreen
import com.example.menzago.ui.screens.auth.RegisterScreen
import com.example.menzago.ui.screens.canteens.CanteenDetailScreen
import com.example.menzago.ui.screens.canteens.CanteensScreen
import com.example.menzago.ui.screens.dish.DishDetailScreen
import com.example.menzago.ui.screens.favorites.FavoritesScreen
import com.example.menzago.ui.screens.home.HomeScreen
import com.example.menzago.ui.screens.profile.ProfileScreen
import com.example.menzago.ui.viewmodel.AuthViewModel
import com.example.menzago.ui.screens.map.MapScreen
import com.example.menzago.ui.screens.admin.AdminMenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    val startDestination = if (authState.isLoggedIn) {
        AppDestination.Home.route
    } else {
        AppDestination.Login.route
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        AppDestination.Home.route,
        AppDestination.Canteens.route,
        AppDestination.Favorites.route,
        AppDestination.Profile.route,
        AppDestination.Map.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MenzaGoBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Login.route) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(AppDestination.Register.route)
                    },
                    onLoginSuccess = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = authViewModel
                )
            }

            composable(AppDestination.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Register.route) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = authViewModel
                )
            }

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
                ProfileScreen(
                    authViewModel = authViewModel,
                    onOpenAdmin = {
                        navController.navigate(AppDestination.AdminMenu.route)
                    },
                    onLogout = {
                        navController.navigate(AppDestination.Login.route) {
                            popUpTo(AppDestination.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(
                route = AppDestination.CanteenDetail.route,
                arguments = listOf(
                    navArgument("canteenId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val canteenId = backStackEntry.arguments?.getInt("canteenId") ?: 1

                CanteenDetailScreen(
                    canteenId = canteenId,
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenDish = { dishId ->
                        navController.navigate(AppDestination.DishDetail.createRoute(dishId))
                    }
                )
            }

            composable(
                route = AppDestination.DishDetail.route,
                arguments = listOf(
                    navArgument("dishId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val dishId = backStackEntry.arguments?.getInt("dishId") ?: 1

                DishDetailScreen(
                    dishId = dishId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppDestination.Map.route) {
                MapScreen(
                    onOpenCanteen = { canteenId ->
                        navController.navigate(AppDestination.CanteenDetail.createRoute(canteenId))
                    }
                )
            }

            composable(AppDestination.AdminMenu.route) {
                AdminMenuScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}