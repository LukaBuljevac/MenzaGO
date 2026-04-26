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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.data.mock.MockData
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.components.SectionHeader
import com.example.menzago.ui.components.StatusBadge

@Composable
fun CanteenDetailScreen(
    canteenId: Int,
    onBack: () -> Unit,
    onOpenDish: (Int) -> Unit
) {
    val canteen = MockData.canteens.firstOrNull { it.id == canteenId } ?: MockData.canteens.first()
    val dishes = MockData.dishes

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
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusBadge(isOpen = canteen.isOpen)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Radno vrijeme: ${canteen.workingHours}",
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
                onClick = { onOpenDish(dish.id) }
            )
        }
    }
}