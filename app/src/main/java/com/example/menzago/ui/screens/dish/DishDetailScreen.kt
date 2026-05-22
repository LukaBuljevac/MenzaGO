package com.example.menzago.ui.screens.dish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.AllergenChip
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.DetailViewModel

@Composable
fun DishDetailScreen(
    dishId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    var refreshKey by remember { mutableStateOf(0) }

    val dish = remember(refreshKey, dishId) {
        viewModel.getDishById(dishId)
    }

    val comments = remember {
        viewModel.getComments()
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(
                title = "Detalji jela",
                showBackButton = true,
                onBackClick = onBack
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fastfood,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Slika jela")
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = dish.category,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.toggleDishFavorite(dish.id)
                        refreshKey++
                    }
                ) {
                    Icon(
                        imageVector = if (dish.isFavorite) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = "Favorite"
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text("${dish.rating}")

                Spacer(modifier = Modifier.width(16.dp))

                Text("${dish.calories} kcal")
            }
        }

        item {
            Text(
                text = dish.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nutritivne vrijednosti",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Kalorije: ${dish.calories} kcal")
                    Text("Kategorija: ${dish.category}")
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Alergeni",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (dish.allergens.isEmpty()) {
                        Text(
                            text = "Nema poznatih alergena.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.wrapContentHeight()
                        ) {
                            dish.allergens.chunked(2).forEach { rowItems ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowItems.forEach { allergen ->
                                        AllergenChip(allergen = allergen)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Komentari",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    comments.forEach { comment ->
                        Text(
                            text = "${comment.userName} (${comment.rating}/5)",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = comment.text,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.toggleDishFavorite(dish.id)
                    refreshKey++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (dish.isFavorite) {
                        "Ukloni iz favorita"
                    } else {
                        "Dodaj u favorite"
                    }
                )
            }
        }
    }
}