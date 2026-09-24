package com.example.jobtracker.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/**
 * Convenience wrapper around WorkManager for scheduling the reminder worker.
 *
 * The worker itself (`ReminderWorker`) is declared in Phase 3. This class only
 * builds the request and enqueues it, so the data layer stays free of worker
 * implementation details.
 */
object ReminderScheduler {

    const val KEY_EVENT_ID = "event_id"
    private const val UNIQUE_PREFIX = "reminder_event_"

    fun schedule(context: Context, eventId: Long, triggerAtMillis: Long) {
        val delayMillis = (triggerAtMillis - System.currentTimeMillis()).coerceAtLeast(0L)

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(KEY_EVENT_ID to eventId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .addTag(tagFor(eventId))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueNameFor(eventId),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel(context: Context, eventId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork(uniqueNameFor(eventId))
    }

    private fun uniqueNameFor(eventId: Long): String = "$UNIQUE_PREFIX$eventId"

    private fun tagFor(eventId: Long): String = "$UNIQUE_PREFIX$eventId"
}
