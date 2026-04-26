package com.example.menzago.ui.screens.home

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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.data.mock.MockData
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.MenzaGoSearchBar
import com.example.menzago.ui.components.SectionHeader
import com.example.menzago.ui.components.StatusBadge

@Composable
fun HomeScreen(
    onSeeAllCanteens: () -> Unit,
    onOpenDish: (Int) -> Unit,
    onOpenCanteen: (Int) -> Unit
) {
    val nearestCanteen = MockData.canteens.first()
    val dishes = MockData.dishes.take(3)
    val previewCanteens = MockData.canteens.take(2)

    val query by remember { mutableStateOf("") }

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
                query = query,
                onQueryChange = {},
                placeholder = "Pretraži jela ili menze"
            )
        }

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

                    androidx.compose.foundation.layout.Row {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(0.dp))
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

        item {
            SectionHeader(
                title = "Danas u ponudi"
            )
        }

        items(dishes) { dish ->
            DishCard(
                dish = dish,
                onClick = { onOpenDish(dish.id) }
            )
        }

        item {
            SectionHeader(
                title = "Sve menze",
                actionText = "Vidi sve",
                onActionClick = onSeeAllCanteens
            )
        }

        items(previewCanteens) { canteen ->
            CanteenCard(
                canteen = canteen,
                onClick = { onOpenCanteen(canteen.id) }
            )
        }
    }
}