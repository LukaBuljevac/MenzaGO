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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.data.model.Review
import com.example.menzago.ui.components.AllergenChip
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.AuthViewModel
import com.example.menzago.ui.viewmodel.DetailViewModel
import com.example.menzago.ui.viewmodel.ReviewViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.menzago.R
import androidx.compose.material3.OutlinedButton

@Composable
fun DishDetailScreen(
    dishId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(),
    reviewViewModel: ReviewViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    var refreshKey by remember { mutableIntStateOf(0) }

    val dish = remember(refreshKey, dishId) {
        viewModel.getDishById(dishId)
    }

    val reviewState by reviewViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val imageRes = remember(dish.imageName) {
        context.resources.getIdentifier(
            dish.imageName,
            "drawable",
            context.packageName
        )
    }

    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(5f) }

    LaunchedEffect(dishId) {
        reviewViewModel.loadReviews(dishId)
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
                Image(
                    painter = painterResource(
                        id = if (imageRes != 0) {
                            imageRes
                        } else {
                            R.drawable.food_placeholder
                        }
                    ),
                    contentDescription = dish.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
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
                        contentDescription = null
                    )
                }
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (reviewState.reviewCount > 0) {
                        "%.1f (%d recenzija)".format(
                            reviewState.averageRating,
                            reviewState.reviewCount
                        )
                    } else {
                        "${dish.rating}"
                    }
                )

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
            Text(
                text = "Komentari i ocjene",
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Dodaj komentar") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Ocjena: ${rating.toInt()}/5")

                    Slider(
                        value = rating,
                        onValueChange = { rating = it },
                        valueRange = 1f..5f,
                        steps = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (comment.isNotBlank()) {
                                reviewViewModel.addReview(
                                    Review(
                                        dishId = dishId,
                                        userEmail = authViewModel.getCurrentEmail(),
                                        comment = comment,
                                        rating = rating.toInt()
                                    )
                                )

                                comment = ""
                                rating = 5f
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Objavi komentar")
                    }
                }
            }
        }

        if (reviewState.isLoading) {
            item {
                Text(
                    text = "Učitavam komentare...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        reviewState.errorMessage?.let { message ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (!reviewState.isLoading && reviewState.reviews.isEmpty()) {
            item {
                Text(
                    text = "Još nema komentara. Budi prvi koji će ocijeniti ovo jelo.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(reviewState.reviews) { review ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = review.userEmail,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = review.comment)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "⭐ ${review.rating}/5")

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            reviewViewModel.deleteReview(
                                reviewId = review.id,
                                dishId = dishId
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Obriši komentar")
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