package com.example.menzago.ui.screens.canteens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.MenzaGoSearchBar
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.CanteensViewModel

@Composable
fun CanteensScreen(
    onOpenCanteen: (Int) -> Unit,
    viewModel: CanteensViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MenzaGoTopBar(title = "Menze")
        }

        item {
            MenzaGoSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Pretraži menze"
            )
        }

        items(uiState.canteens) { canteen ->
            CanteenCard(
                canteen = canteen,
                onClick = { onOpenCanteen(canteen.id) },
                onFavoriteClick = viewModel::toggleCanteenFavorite
            )
        }
    }
}