package com.example.richnoteapp.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.richnoteapp.data.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY title ASC")
    fun getNotes(): LiveData<List<Note>>

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
