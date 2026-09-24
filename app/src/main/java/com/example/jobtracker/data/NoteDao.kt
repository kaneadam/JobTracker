package com.example.jobtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * DAO for [NoteEntity].
 *
 * All read methods are `suspend` (not Flow), per project rules.
 */
@Dao
interface NoteDao {

    // ---------- Writes ----------

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>): List<Long>

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM notes WHERE eventId = :eventId")
    suspend fun deleteForEvent(eventId: Long)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    // ---------- Reads (suspend, no Flow) ----------

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes WHERE eventId = :eventId ORDER BY createdAt ASC")
    suspend fun getForEvent(eventId: Long): List<NoteEntity>

    @Query("SELECT * FROM notes ORDER BY createdAt ASC")
    suspend fun getAll(): List<NoteEntity>

    @Query("SELECT COUNT(*) FROM notes WHERE eventId = :eventId")
    suspend fun countForEvent(eventId: Long): Int
}
