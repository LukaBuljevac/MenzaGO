package com.example.menzago

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.menzago.ui.navigation.AppNavigation
import com.example.menzago.ui.theme.MenzaGOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenzaGOTheme {
                AppNavigation()
            }
        }
    }
}