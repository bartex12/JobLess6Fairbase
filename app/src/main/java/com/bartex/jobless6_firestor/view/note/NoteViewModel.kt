package com.bartex.jobless6_firestor.view.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult
import com.bartex.jobless6_firestor.repo.NoteRepository

class NoteViewModel(private  val repository:NoteRepository = NoteRepository)
    : ViewModel() {
    val viewStateLiveData = MutableLiveData<NoteViewState>()
    fun getViewState(): LiveData<NoteViewState> = viewStateLiveData

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        val repositoryNote: LiveData<NoteResult> = repository.getNoteById(noteId)

        repositoryNote.observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return
                when (t) {
                    is NoteResult.Success<*> ->
                        viewStateLiveData.value =
                            NoteViewState( note = t.data as? Note)
                    is NoteResult.Error ->
                        viewStateLiveData.value =
                            NoteViewState(
                                error = t.error
                            )
                }
            }
        })
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}