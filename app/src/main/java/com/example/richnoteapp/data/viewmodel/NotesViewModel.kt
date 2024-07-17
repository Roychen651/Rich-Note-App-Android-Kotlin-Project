package com.example.richnoteapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.richnoteapp.data.model.Note
import com.example.richnoteapp.data.respository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository(application)

    val notes: LiveData<List<Note>>? = repository.getNotes()

    private val _selectedNote = MutableLiveData<Note>()
    val selectedNote: LiveData<Note> get() = _selectedNote

    private val _chosenNote = MutableLiveData<Note>()
    val chosenNote: LiveData<Note> get() = _chosenNote

    fun setNote(note: Note) {
        _chosenNote.value = note
        _selectedNote.value = note
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
