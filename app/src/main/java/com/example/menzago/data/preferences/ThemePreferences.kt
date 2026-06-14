package com.example.menzago.data.preferences

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemePreferences {

    private const val PREF_NAME = "menzago_prefs"
    private const val KEY_DARK_MODE = "dark_mode"

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        _isDarkMode.value = prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun toggle(context: Context) {
        val newValue = !_isDarkMode.value

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        prefs.edit()
            .putBoolean(KEY_DARK_MODE, newValue)
            .apply()

        _isDarkMode.value = newValue
    }
}