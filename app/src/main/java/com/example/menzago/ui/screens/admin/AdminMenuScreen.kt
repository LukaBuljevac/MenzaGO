package com.example.menzago.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.AdminMenuViewModel

@Composable
fun AdminMenuScreen(
    onBack: () -> Unit,
    viewModel: AdminMenuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var dishId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var allergens by remember { mutableStateOf("") }
    var imageName by remember { mutableStateOf("") }

    var canteenId by remember { mutableStateOf("1") }
    var dishIds by remember { mutableStateOf("") }
    var removeDishId by remember { mutableStateOf("") }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MenzaGoTopBar(
                    title = "Admin meni",
                    showBackButton = true,
                    onBackClick = onBack
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Dodaj ili ažuriraj jelo",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = dishId,
                            onValueChange = { dishId = it },
                            label = { Text("ID jela") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Naziv") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Opis") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Kategorija") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = calories,
                            onValueChange = { calories = it },
                            label = { Text("Kalorije") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = allergens,
                            onValueChange = { allergens = it },
                            label = { Text("Alergeni, odvojeni zarezom") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = imageName,
                            onValueChange = { imageName = it },
                            label = { Text("Naziv slike, npr. piletina") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.addOrUpdateDish(
                                    id = dishId.toIntOrNull(),
                                    name = name,
                                    description = description,
                                    category = category,
                                    calories = calories.toIntOrNull(),
                                    allergens = allergens
                                        .split(",")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() },
                                    imageName = imageName.trim()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        ) {
                            Text("Spremi jelo")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.deleteDish(
                                    dishId = dishId.toIntOrNull()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        ) {
                            Text("Obriši jelo")
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Postavi današnji meni",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = canteenId,
                            onValueChange = { canteenId = it },
                            label = { Text("ID menze") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = dishIds,
                            onValueChange = { dishIds = it },
                            label = { Text("ID-jevi jela, npr. 1,2,5") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.saveDailyMenu(
                                    canteenId = canteenId.toIntOrNull(),
                                    dishIds = dishIds
                                        .split(",")
                                        .mapNotNull { it.trim().toIntOrNull() }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        ) {
                            Text("Spremi dnevni meni")
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = removeDishId,
                            onValueChange = { removeDishId = it },
                            label = { Text("ID jela za ukloniti s menija") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.removeDishFromDailyMenu(
                                    canteenId = canteenId.toIntOrNull(),
                                    dishId = removeDishId.toIntOrNull()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        ) {
                            Text("Ukloni jelo s dnevnog menija")
                        }
                    }
                }
            }
        }
    }
}