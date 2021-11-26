package com.bartex.jobless6_firestor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.jobless6_firestor.MainViewState
import com.bartex.jobless6_firestor.repo.NoteRepository

class MainViewModel: ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init{
        //если viewStateLiveData.value не равна нулю, копируем ее, а если равна -
        //получаем ее как  value из LiveData из  класса MainViewState
        NoteRepository.getNote().observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it) ?: MainViewState(it)
        }
    }

    // Возвращаемое значение функции viewState() — LiveData (предок для MutableLiveData),
    // в которую нельзя передать новое значение, а можно только получить имеющееся.
    fun viewState(): LiveData<MainViewState> {
        return viewStateLiveData
    }
}