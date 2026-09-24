package com.example.jobtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

/**
 * DAO for [EventEntity].
 *
 * Per project rules, all read methods here are `suspend` (not Flow) so they can be
 * called safely from WorkManager workers and from coroutines without observing.
 */
@Dao
interface EventDao {

    // ---------- Writes ----------

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>): List<Long>

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    // ---------- Reads (suspend, no Flow) ----------

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): EventEntity?

    @Query("SELECT * FROM events ORDER BY startAt ASC")
    suspend fun getAll(): List<EventEntity>

    @Query("SELECT * FROM events WHERE status = :status ORDER BY startAt ASC")
    suspend fun getByStatus(status: String): List<EventEntity>

    @Query(
        """
        SELECT * FROM events
        WHERE reminderEnabled = 1
          AND reminderFired = 0
          AND reminderAt IS NOT NULL
          AND reminderAt <= :now
        ORDER BY reminderAt ASC
        """
    )
    suspend fun getDueReminders(now: Long): List<EventEntity>

    @Query(
        """
        SELECT * FROM events
        WHERE startAt >= :from AND startAt < :to
        ORDER BY startAt ASC
        """
    )
    suspend fun getBetween(from: Long, to: Long): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun count(): Int

    // ---------- Reminder bookkeeping ----------

    @Query("UPDATE events SET reminderFired = 1, updatedAt = :now WHERE id = :id")
    suspend fun markReminderFired(id: Long, now: Long)

    // ---------- Relations ----------

    @Transaction
    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventWithNotes(id: Long): EventWithNotes?

    @Transaction
    @Query("SELECT * FROM events ORDER BY startAt ASC")
    suspend fun getAllEventsWithNotes(): List<EventWithNotes>
}
