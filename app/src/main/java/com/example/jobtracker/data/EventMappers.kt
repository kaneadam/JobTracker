package com.example.jobtracker.data

/**
 * Mapping helpers between Room entities and lightweight domain models.
 *
 * Kept in one place so ViewModels and workers don't each reinvent the
 * enum-from-string parsing.
 */

fun EventEntity.typeEnum(): EventType =
    runCatching { EventType.valueOf(type) }.getOrDefault(EventType.OTHER)

fun EventEntity.statusEnum(): EventStatus =
    runCatching { EventStatus.valueOf(status) }.getOrDefault(EventStatus.SCHEDULED)

fun EventEntity.withType(value: EventType): EventEntity =
    copy(type = value.name)

fun EventEntity.withStatus(value: EventStatus): EventEntity =
    copy(status = value.name)

/**
 * Lightweight, UI-facing snapshot of an event.
 * Use this when you don't need the raw entity or its notes.
 */
data class EventSummary(
    val id: Long,
    val title: String,
    val company: String?,
    val role: String?,
    val type: EventType,
    val status: EventStatus,
    val startAt: Long,
    val endAt: Long?,
    val noteCount: Int
)

fun EventEntity.toSummary(noteCount: Int = 0): EventSummary = EventSummary(
    id = id,
    title = title,
    company = company,
    role = role,
    type = typeEnum(),
    status = statusEnum(),
    startAt = startAt,
    endAt = endAt,
    noteCount = noteCount
)
