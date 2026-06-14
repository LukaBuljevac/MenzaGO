package com.example.menzago.data.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object CanteenStatusUtils {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    fun isOpenNow(workingHours: String): Boolean {
        return try {
            val parts = workingHours
                .replace(" ", "")
                .split("-")

            if (parts.size != 2) return false

            val openTime = LocalTime.parse(parts[0], formatter)
            val closeTime = LocalTime.parse(parts[1], formatter)
            val now = LocalTime.now()

            if (closeTime.isAfter(openTime)) {
                now.isAfter(openTime) && now.isBefore(closeTime)
            } else {
                now.isAfter(openTime) || now.isBefore(closeTime)
            }
        } catch (e: Exception) {
            false
        }
    }
}