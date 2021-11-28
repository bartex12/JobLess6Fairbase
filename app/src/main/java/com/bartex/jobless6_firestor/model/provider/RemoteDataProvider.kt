package com.bartex.jobless6_firestor.model.provider

import androidx.lifecycle.LiveData
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>

}