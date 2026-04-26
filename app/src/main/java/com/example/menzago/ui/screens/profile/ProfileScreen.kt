package com.example.menzago.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.ui.components.MenzaGoTopBar

@Composable
fun ProfileScreen() {
    val notificationsEnabled = remember { mutableStateOf(true) }
    val darkModeEnabled = remember { mutableStateOf(false) }

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(72.dp)
                            .fillMaxWidth(0.2f)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Student Korisnik",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "student@menzago.hr",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card {
                Column {
                    ListItem(
                        headlineContent = { Text("Notifikacije") },
                        supportingContent = { Text("Obavijesti za omiljena jela") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = notificationsEnabled.value,
                                onCheckedChange = { notificationsEnabled.value = it }
                            )
                        }
                    )

                    ListItem(
                        headlineContent = { Text("Tamna tema") },
                        supportingContent = { Text("Uključi dark mode") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.DarkMode,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = darkModeEnabled.value,
                                onCheckedChange = { darkModeEnabled.value = it }
                            )
                        }
                    )

                    ListItem(
                        headlineContent = { Text("Račun") },
                        supportingContent = { Text("Uredi profil i prijavu") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null
                            )
                        }
                    )

                    ListItem(
                        headlineContent = { Text("Postavke aplikacije") },
                        supportingContent = { Text("Dodatne opcije") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}