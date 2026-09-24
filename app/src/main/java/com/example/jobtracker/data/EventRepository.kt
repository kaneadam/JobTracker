package com.example.jobtracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository wrapping [EventDao] and [NoteDao].
 *
 * All methods are `suspend` so they can be called from ViewModels, workers, or
 * anywhere else on a coroutine. I/O runs on [Dispatchers.IO].
 */
class EventRepository(
    private val eventDao: EventDao,
    private val noteDao: NoteDao
) {

    // ---------- Events ----------

    suspend fun insertEvent(event: EventEntity): Long = withContext(Dispatchers.IO) {
        eventDao.insert(event)
    }

    suspend fun insertEvents(events: List<EventEntity>): List<Long> = withContext(Dispatchers.IO) {
        eventDao.insertAll(events)
    }

    suspend fun updateEvent(event: EventEntity) = withContext(Dispatchers.IO) {
        eventDao.update(event.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteEvent(event: EventEntity) = withContext(Dispatchers.IO) {
        eventDao.delete(event)
    }

    suspend fun deleteEventById(id: Long) = withContext(Dispatchers.IO) {
        eventDao.deleteById(id)
    }

    suspend fun getEvent(id: Long): EventEntity? = withContext(Dispatchers.IO) {
        eventDao.getById(id)
    }

    suspend fun getAllEvents(): List<EventEntity> = withContext(Dispatchers.IO) {
        eventDao.getAll()
    }

    suspend fun getEventsByStatus(status: EventStatus): List<EventEntity> = withContext(Dispatchers.IO) {
        eventDao.getByStatus(status.name)
    }

    suspend fun getEventsBetween(from: Long, to: Long): List<EventEntity> = withContext(Dispatchers.IO) {
        eventDao.getBetween(from, to)
    }

    suspend fun getDueReminders(now: Long): List<EventEntity> = withContext(Dispatchers.IO) {
        eventDao.getDueReminders(now)
    }

    suspend fun markReminderFired(id: Long) = withContext(Dispatchers.IO) {
        eventDao.markReminderFired(id, System.currentTimeMillis())
    }

    suspend fun countEvents(): Int = withContext(Dispatchers.IO) {
        eventDao.count()
    }

    // ---------- Relations ----------

    suspend fun getEventWithNotes(id: Long): EventWithNotes? = withContext(Dispatchers.IO) {
        eventDao.getEventWithNotes(id)
    }

    suspend fun getAllEventsWithNotes(): List<EventWithNotes> = withContext(Dispatchers.IO) {
        eventDao.getAllEventsWithNotes()
    }

    // ---------- Notes ----------

    suspend fun insertNote(note: NoteEntity): Long = withContext(Dispatchers.IO) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.update(note.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.delete(note)
    }

    suspend fun deleteNoteById(id: Long) = withContext(Dispatchers.IO) {
        noteDao.deleteById(id)
    }

    suspend fun deleteNotesForEvent(eventId: Long) = withContext(Dispatchers.IO) {
        noteDao.deleteForEvent(eventId)
    }

    suspend fun getNote(id: Long): NoteEntity? = withContext(Dispatchers.IO) {
        noteDao.getById(id)
    }

    suspend fun getNotesForEvent(eventId: Long): List<NoteEntity> = withContext(Dispatchers.IO) {
        noteDao.getForEvent(eventId)
    }

    suspend fun getAllNotes(): List<NoteEntity> = withContext(Dispatchers.IO) {
        noteDao.getAll()
    }

    suspend fun countNotesForEvent(eventId: Long): Int = withContext(Dispatchers.IO) {
        noteDao.countForEvent(eventId)
    }
}
