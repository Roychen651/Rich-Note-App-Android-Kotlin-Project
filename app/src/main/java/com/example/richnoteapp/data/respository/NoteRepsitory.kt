package com.example.richnoteapp.data.respository

import android.app.Application
import com.example.richnoteapp.data.local_db.NoteDao
import com.example.richnoteapp.data.model.Note
import com.example.richnoteapp.data.local_db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(application: Application) {

    private val noteDao: NoteDao? = NoteDatabase.getDatabase(application.applicationContext).noteDao()

    suspend fun addNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao?.addNote(note)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao?.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao?.deleteNote(note)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            noteDao?.deleteAll()
        }
    }

    fun getNotes() = noteDao?.getNotes()
}
