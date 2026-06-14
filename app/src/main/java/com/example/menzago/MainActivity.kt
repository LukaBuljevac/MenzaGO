package com.example.menzago

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.menzago.ui.navigation.AppNavigation
import com.example.menzago.ui.theme.MenzaGOTheme
import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.menzago.workers.FavoriteDishWorker
import java.util.concurrent.TimeUnit
import com.example.menzago.notifications.MenzaNotificationHelper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.menzago.data.preferences.ThemePreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        MenzaNotificationHelper.createChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {}.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val request = PeriodicWorkRequestBuilder<FavoriteDishWorker>(
            12,
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this)
            .enqueue(request)

        ThemePreferences.load(this)

        setContent {
            val isDarkMode by ThemePreferences.isDarkMode.collectAsState()

            MenzaGOTheme(
                darkTheme = isDarkMode
            ) {
                AppNavigation()
            }
        }
    }
}