package com.example.jobtracker.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room relation POJO: an [EventEntity] plus all of its [NoteEntity] rows.
 */
data class EventWithNotes(
    @Embedded
    val event: EventEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val notes: List<NoteEntity>
)
