package com.bartex.jobless6_firestor.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult
import com.bartex.jobless6_firestor.repo.NoteRepository
import java.util.*

class MainViewModel : ViewModel() {

    val viewStateLiveData = MutableLiveData<MainViewState>()
    fun getViewState():LiveData<MainViewState> = viewStateLiveData

    private var repositoryNotes: LiveData<NoteResult>? = null

    fun loadNotes() {
        repositoryNotes = NoteRepository.getNotes()
        repositoryNotes?.observeForever(notesObserver)
    }

    //здесь  notesObserver вынесена в отдельную переменную для отвязки в onCleared
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when(t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value =
                        MainViewState(notes = (t.data as? List<Note>))
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value =
                        MainViewState(error = t.error)
                }
            }
        }
    }

    override fun onCleared() {
        //отписка от наблюдателя чтобы избежать утечки памяти
        repositoryNotes?.removeObserver(notesObserver)
        super.onCleared()
    }
}