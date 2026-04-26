package com.example.menzago.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.MenzaGoSearchBar
import com.example.menzago.ui.components.SectionHeader
import com.example.menzago.ui.components.StatusBadge
import com.example.menzago.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onSeeAllCanteens: () -> Unit,
    onOpenDish: (Int) -> Unit,
    onOpenCanteen: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val nearestCanteen = uiState.nearestCanteen

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Bok, student!",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Što ćemo danas jesti?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            MenzaGoSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Pretraži jela ili menze"
            )
        }

        if (nearestCanteen != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Najbliža menza",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = nearestCanteen.name,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null
                            )
                            Text(" ${nearestCanteen.distanceMeters} m")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        StatusBadge(isOpen = nearestCanteen.isOpen)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Radno vrijeme: ${nearestCanteen.workingHours}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onOpenCanteen(nearestCanteen.id) }
                        ) {
                            Text("Pogledaj meni")
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Danas u ponudi")
        }

        items(uiState.todayDishes) { dish ->
            DishCard(
                dish = dish,
                onClick = { onOpenDish(dish.id) },
                onFavoriteClick = viewModel::toggleDishFavorite
            )
        }

        item {
            SectionHeader(
                title = "Sve menze",
                actionText = "Vidi sve",
                onActionClick = onSeeAllCanteens
            )
        }

        items(uiState.previewCanteens) { canteen ->
            CanteenCard(
                canteen = canteen,
                onClick = { onOpenCanteen(canteen.id) },
                onFavoriteClick = viewModel::toggleCanteenFavorite
            )
        }
    }
}