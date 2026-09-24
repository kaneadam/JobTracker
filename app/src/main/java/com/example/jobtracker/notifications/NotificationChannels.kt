package com.example.jobtracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Central definition + registration of notification channels.
 *
 * Call [ensureChannels] early (e.g. from Application.onCreate) so the channel
 * exists before any notification is posted on API 26+.
 */
object NotificationChannels {

    const val REMINDERS_CHANNEL_ID = "jobtracker_reminders"
    const val REMINDERS_CHANNEL_NAME = "Event Reminders"
    const val REMINDERS_CHANNEL_DESC = "Reminders for scheduled job-tracker events."

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        val existing = manager.getNotificationChannel(REMINDERS_CHANNEL_ID)
        if (existing == null) {
            val channel = NotificationChannel(
                REMINDERS_CHANNEL_ID,
                REMINDERS_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = REMINDERS_CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }
    }
}
