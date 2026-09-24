package com.example.jobtracker.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.jobtracker.MainActivity
import com.example.jobtracker.R
import com.example.jobtracker.data.EventEntity

/**
 * Builds and posts the reminder notification for a given [EventEntity].
 *
 * Safe to call on all API levels; silently no-ops on API 33+ if the
 * POST_NOTIFICATIONS permission has not been granted.
 */
object ReminderNotifier {

    fun show(context: Context, event: EventEntity) {
        NotificationChannels.ensureChannels(context)

        // On API 33+ we must have runtime permission before posting.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            event.id.toInt(),
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_EVENT_ID, event.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = event.title.ifBlank { "Job tracker reminder" }
        val body = buildBody(event)

        val notification = NotificationCompat.Builder(context, NotificationChannels.REMINDERS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        NotificationManagerCompat.from(context).notify(event.id.toInt(), notification)
    }

    private fun buildBody(event: EventEntity): String {
        val parts = mutableListOf<String>()
        event.company?.takeIf { it.isNotBlank() }?.let { parts += it }
        event.role?.takeIf { it.isNotBlank() }?.let { parts += it }
        event.description?.takeIf { it.isNotBlank() }?.let { parts += it }
        return if (parts.isEmpty()) "You have an upcoming event." else parts.joinToString(" • ")
    }

    const val EXTRA_EVENT_ID = "extra_event_id"
}
