package com.example.jobtracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "events",
    indices = [
        Index(value = ["startAt"]),
        Index(value = ["status"])
    ]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** Short title shown in lists, e.g. "Phone screen — Acme". */
    val title: String,

    /** Optional free-form description. */
    val description: String? = null,

    /** Company or org associated with the event. */
    val company: String? = null,

    /** Role / position associated with the event. */
    val role: String? = null,

    /** Event type: INTERVIEW, FOLLOW_UP, DEADLINE, etc. Stored as String. */
    val type: String = EventType.OTHER.name,

    /** Current status: SCHEDULED, DONE, CANCELED. Stored as String. */
    val status: String = EventStatus.SCHEDULED.name,

    /** Epoch millis when the event starts. */
    val startAt: Long,

    /** Epoch millis when the event ends. Null for point-in-time events. */
    val endAt: Long? = null,

    /** Whether a reminder should be scheduled by the worker. */
    val reminderEnabled: Boolean = false,

    /** Epoch millis for the reminder. Null unless reminderEnabled is true. */
    val reminderAt: Long? = null,

    /** Whether the reminder has already fired (avoids duplicate notifications). */
    val reminderFired: Boolean = false,

    /** Creation timestamp in epoch millis. */
    val createdAt: Long = Instant.now().toEpochMilli(),

    /** Last update timestamp in epoch millis. */
    val updatedAt: Long = Instant.now().toEpochMilli()
)

enum class EventType {
    INTERVIEW,
    FOLLOW_UP,
    DEADLINE,
    OFFER,
    REJECTION,
    OTHER
}

enum class EventStatus {
    SCHEDULED,
    DONE,
    CANCELED
}
