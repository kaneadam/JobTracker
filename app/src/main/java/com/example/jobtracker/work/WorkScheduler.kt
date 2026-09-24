package com.example.jobtracker.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Registers the app's periodic background work.
 *
 * Call [ensureScheduled] once from Application.onCreate (idempotent thanks to
 * KEEP policy). One-shot reminders are scheduled separately via
 * [com.example.jobtracker.data.ReminderScheduler].
 */
object WorkScheduler {

    private const val SCAN_INTERVAL_MINUTES = 15L

    fun ensureScheduled(context: Context) {
        val request = PeriodicWorkRequestBuilder<ReminderScanWorker>(
            SCAN_INTERVAL_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ReminderScanWorker.UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
