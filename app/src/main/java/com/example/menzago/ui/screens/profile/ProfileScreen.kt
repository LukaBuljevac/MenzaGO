package com.example.menzago.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.ui.components.MenzaGoTopBar
import com.example.menzago.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            MenzaGoTopBar(title = "Profil")
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Prijavljeni korisnik",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = authViewModel.getCurrentEmail(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Odjava")
            }
        }
    }
}