package com.bartex.jobless6_firestor.repo

import androidx.lifecycle.LiveData
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult
import com.bartex.jobless6_firestor.model.provider.FireStoreProvider
import com.bartex.jobless6_firestor.model.provider.RemoteDataProvider

// Repository только переадресует вызовы методов RemoteDataProvider
object NoteRepository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes(): LiveData<NoteResult> = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note):LiveData<NoteResult> = remoteProvider.saveNote(note)
    fun getNoteById(id: String):LiveData<NoteResult> = remoteProvider.getNoteById(id)
}