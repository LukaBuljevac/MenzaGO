package com.example.menzago.ui.screens.canteens

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
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.components.SectionHeader
import com.example.menzago.ui.components.StatusBadge
import com.example.menzago.ui.viewmodel.DetailViewModel

@Composable
fun CanteenDetailScreen(
    canteenId: Int,
    onBack: () -> Unit,
    onOpenDish: (Int) -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    var refreshKey by remember { mutableStateOf(0) }

    val canteen = remember(refreshKey, canteenId) {
        viewModel.getCanteenById(canteenId)
    }

    val dishes = remember(refreshKey) {
        viewModel.getAllDishes()
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(
                title = "Detalji menze",
                showBackButton = true,
                onBackClick = onBack
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = canteen.name,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = canteen.location,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.toggleCanteenFavorite(canteen.id)
                                refreshKey++
                            }
                        ) {
                            Icon(
                                imageVector = if (canteen.isFavorite) {
                                    Icons.Outlined.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = "Favorite"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusBadge(isOpen = canteen.isOpen)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Radno vrijeme: ${canteen.workingHours}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Udaljenost: ${canteen.distanceMeters} m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Današnja ponuda")
        }

        items(dishes) { dish ->
            DishCard(
                dish = dish,
                onClick = { onOpenDish(dish.id) },
                onFavoriteClick = {
                    viewModel.toggleDishFavorite(it)
                    refreshKey++
                }
            )
        }
    }
}