package com.example.menzago.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.outlined.Map

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Početna",
        icon = Icons.Outlined.Home,
        route = AppDestination.Home.route
    ),
    BottomNavItem(
        label = "Menze",
        icon = Icons.Outlined.Restaurant,
        route = AppDestination.Canteens.route
    ),
    BottomNavItem(
        label = "Favoriti",
        icon = Icons.Outlined.FavoriteBorder,
        route = AppDestination.Favorites.route
    ),
    BottomNavItem(
        label = "Profil",
        icon = Icons.Outlined.Person,
        route = AppDestination.Profile.route
    ),
    BottomNavItem(
        label = "Mapa",
        icon = Icons.Outlined.Map,
        route = AppDestination.Map.route
    )
)