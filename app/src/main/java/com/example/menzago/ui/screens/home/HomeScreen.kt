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
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.CanteenCard
import com.example.menzago.ui.components.DishCard
import com.example.menzago.ui.components.MenzaGoSearchBar
import com.example.menzago.ui.components.SectionHeader
import com.example.menzago.ui.components.StatusBadge
import com.example.menzago.ui.viewmodel.HomeViewModel
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.example.menzago.sensors.ShakeDetector
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.menzago.data.location.LocationRepository
import com.example.menzago.data.location.UserLocationRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.menzago.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    onSeeAllCanteens: () -> Unit,
    onOpenDish: (Int) -> Unit,
    onOpenCanteen: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val nearestCanteen = uiState.nearestCanteen
    val displayName = authViewModel.getCurrentDisplayName()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    fun refreshLocationIfAllowed() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return

        scope.launch {
            val location = LocationRepository(
                context.applicationContext
            ).getCurrentLocation()

            if (location != null) {
                UserLocationRepository.setLocation(location)
            }
        }
    }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val shakeDetector = ShakeDetector {
            viewModel.refreshMenu()
            refreshLocationIfAllowed()
        }

        if (accelerometer != null) {
            sensorManager.registerListener(
                shakeDetector,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            sensorManager.unregisterListener(shakeDetector)
        }
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Bok, $displayName!",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Pametniji pregled studentske prehrane.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(
                    onClick = viewModel::refreshMenu,
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Osvježi"
                    )
                }
            }
        }

        if (uiState.isLoading) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()

                        Text(
                            text = "  Osvježavam današnji meni...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        uiState.errorMessage?.let { message ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = viewModel::refreshMenu) {
                            Text("Pokušaj ponovno")
                        }
                    }
                }
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Najbliža menza",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = nearestCanteen.name,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onOpenCanteen(nearestCanteen.id) },
                            modifier = Modifier.fillMaxWidth()
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

        if (uiState.todayDishes.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Restaurant,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Nema pronađenih jela",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Pokušaj s drugim pojmom pretrage.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(uiState.todayDishes) { dish ->
                DishCard(
                    dish = dish,
                    onClick = { onOpenDish(dish.id) },
                    onFavoriteClick = viewModel::toggleDishFavorite
                )
            }
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