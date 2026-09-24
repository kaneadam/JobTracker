package com.example.jobtracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["eventId"])
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** FK to the owning event. Cascades on delete. */
    val eventId: Long,

    /** Note body text. */
    val content: String,

    /** Creation timestamp in epoch millis. */
    val createdAt: Long = Instant.now().toEpochMilli(),

    /** Last update timestamp in epoch millis. */
    val updatedAt: Long = Instant.now().toEpochMilli()
)
