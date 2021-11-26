package com.bartex.jobless6_firestor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.repo.NoteRepository

class NoteViewModel(private  val repository: NoteRepository = NoteRepository): ViewModel() {
    private val TAG = "33333"
    private var pendingNote: Note? = null

    fun  saveChanges(note: Note){
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?. let{
            Log.d(TAG, "NoteViewModel onCleared note title = " + pendingNote?.titleDate)
            repository.saveNote(pendingNote!!)
        }
    }
}