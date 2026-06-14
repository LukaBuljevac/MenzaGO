package com.example.menzago.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.DishCard
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

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(title = "Favoriti")
        }

        item {
            Text(
                text = "Tvoja spremljena jela i menze nalaze se ovdje.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Jela (${uiState.favoriteDishes.size})") }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Menze (${uiState.favoriteCanteens.size})") }
                )
            }
        }

        if (selectedTab == 0) {
            if (uiState.favoriteDishes.isEmpty()) {
                item {
                    FavoriteEmptyState(
                        title = "Nema omiljenih jela",
                        description = "Dodaj jela u favorite klikom na ikonu srca."
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
                    FavoriteEmptyState(
                        title = "Nema omiljenih menzi",
                        description = "Spremi menze koje najčešće koristiš."
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

@Composable
private fun FavoriteEmptyState(
    title: String,
    description: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}