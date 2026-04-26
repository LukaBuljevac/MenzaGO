package com.example.menzago.ui.screens.canteens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.data.mock.MockData
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.MenzaGoTopBar

@Composable
fun CanteensScreen(
    onOpenCanteen: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(title = "Menze")
        }

        items(MockData.canteens) { canteen ->
            CanteenCard(
                canteen = canteen,
                onClick = { onOpenCanteen(canteen.id) }
            )
        }
    }
}