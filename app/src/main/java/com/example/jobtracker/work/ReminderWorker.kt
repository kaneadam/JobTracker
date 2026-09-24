package com.example.jobtracker.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jobtracker.data.RepositoryProvider
import com.example.jobtracker.data.ReminderScheduler
import com.example.jobtracker.notifications.ReminderNotifier

/**
 * Fires a single reminder for one event.
 *
 * Input: [ReminderScheduler.KEY_EVENT_ID] → Long event id.
 * Reads the event, posts a notification, then marks `reminderFired = true`.
 */
class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val eventId = inputData.getLong(ReminderScheduler.KEY_EVENT_ID, -1L)
        if (eventId <= 0L) return Result.failure()

        val repo = RepositoryProvider.get(applicationContext)
        val event = repo.getEvent(eventId) ?: return Result.success()

        // Skip if the event was canceled or the reminder was already fired.
        if (event.reminderFired) return Result.success()
        if (event.status == com.example.jobtracker.data.EventStatus.CANCELED.name) {
            return Result.success()
        }
        if (!event.reminderEnabled) return Result.success()

        ReminderNotifier.show(applicationContext, event)
        repo.markReminderFired(eventId)
        return Result.success()
    }

    companion object {
        const val UNIQUE_NAME_PREFIX = "reminder_event_"
    }
}
