package com.example.jobtracker.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jobtracker.data.RepositoryProvider
import com.example.jobtracker.notifications.ReminderNotifier

/**
 * Periodic safety-net worker that scans for any reminders whose scheduled time
 * has passed but which have not yet fired (e.g. a one-shot ReminderWorker was
 * dropped by the OS). Catches missed reminders within the periodic window.
 */
class ReminderScanWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repo = RepositoryProvider.get(applicationContext)
        val due = repo.getDueReminders(System.currentTimeMillis())

        for (event in due) {
            ReminderNotifier.show(applicationContext, event)
            repo.markReminderFired(event.id)
        }
        return Result.success()
    }

    companion object {
        const val UNIQUE_NAME = "reminder_scan"
    }
}
