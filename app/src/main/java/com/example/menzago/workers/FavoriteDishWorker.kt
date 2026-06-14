package com.example.menzago.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.menzago.notifications.MenzaNotificationHelper

class FavoriteDishWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        MenzaNotificationHelper.showNotification(
            context = applicationContext,
            title = "MenzaGO",
            message = "Danas je tvoje omiljeno jelo u ponudi!"
        )

        return Result.success()
    }
}