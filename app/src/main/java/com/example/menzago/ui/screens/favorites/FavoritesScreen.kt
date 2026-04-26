package com.example.menzago.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.EmptyStateView
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onOpenDish: (Int) -> Unit,
    onOpenCanteen: (Int) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshFavorites()
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(title = "Favoriti")
        }

        item {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Jela") }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Menze") }
                )
            }
        }

        if (selectedTab == 0) {
            if (uiState.favoriteDishes.isEmpty()) {
                item {
                    EmptyStateView(
                        title = "Nema omiljenih jela",
                        description = "Dodaj jela u favorite kako bi ih ovdje vidio."
                    )
                }
            } else {
                items(uiState.favoriteDishes) { dish ->
                    DishCard(
                        dish = dish,
                        onClick = { onOpenDish(dish.id) },
                        onFavoriteClick = viewModel::toggleDishFavorite
                    )
                }
            }
        } else {
            if (uiState.favoriteCanteens.isEmpty()) {
                item {
                    EmptyStateView(
                        title = "Nema omiljenih menzi",
                        description = "Dodaj menze u favorite kako bi ih ovdje vidio."
                    )
                }
            } else {
                items(uiState.favoriteCanteens) { canteen ->
                    CanteenCard(
                        canteen = canteen,
                        onClick = { onOpenCanteen(canteen.id) },
                        onFavoriteClick = viewModel::toggleCanteenFavorite
                    )
                }
            }
        }
    }
}