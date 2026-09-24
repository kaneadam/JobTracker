package com.example.jobtracker.data

import android.content.Context

/**
 * Minimal service-locator for the repository.
 *
 * Keeps the placeholder project dependency-free (no Hilt / Koin) while still
 * giving workers and ViewModels a single access point. Replace with DI later.
 */
object RepositoryProvider {

    @Volatile
    private var repository: EventRepository? = null

    fun get(context: Context): EventRepository {
        return repository ?: synchronized(this) {
            repository ?: build(context.applicationContext).also { repository = it }
        }
    }

    private fun build(appContext: Context): EventRepository {
        val db = AppDatabase.get(appContext)
        return EventRepository(
            eventDao = db.eventDao(),
            noteDao = db.noteDao()
        )
    }
}
