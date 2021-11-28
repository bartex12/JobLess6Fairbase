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


/*класс MainViewModel является и наблюдателем и наблюдаемым
* он наблюдает за NoteRepository через observeForever без привязки к жизненному циклу
* если что - то в репозитории меняется, изменяется и viewStateLiveData.value
* а изменения  в репозитории происходят при редактировани/создании заметки
*
* а за ним наблюдают через fun viewState(), при этом изменения передаются в адаптер
* для отображения на экране
* Так как ссылка на LiveData, полученную из метода FireStoreProvider,
*  останется в экземпляре EventListener, нужно отписаться от этой LiveData при ее уничтожении,
*  чтобы избежать утечек памяти. Для этого вынесем полученную LiveData в поле repositoryNotes
*  и в блоке init подпишемся на нее, а в методе onCleared(),
* который вызывается при уничтожении ViewModel, отпишемся.
* В методе onChanged свойства noteObserver видим, как используют sealed-класс в блоке when.
* Здесь проверяем, какой из наследников NoteResult вернула LiveData, и в зависимости от результата
*  проверки передаем экземпляр MainViewState с данными или с ошибкой
* в viewStateLiveData из базовой ViewModel. Из-за стирания типов не можем
* проверить тип параметра класса Success, поэтому используем проекцию со звездочкой.
* */

class MainViewModel( ) : ViewModel() {

    companion object{
        private const val TAG = "33333"
    }

    val viewStateLiveData = MutableLiveData<MainViewState>()
    fun getViewState():LiveData<MainViewState> = viewStateLiveData

    //можно также  передать repositoryNotes в конструкторе как это сделано в NoteViewModel
    // и подписку на repositoryNotes сделать в блоке init
    //тогда в MainActivity не нужно вызывать loadNotes, всё автоматом произойдёт
    // при инициализации ViewModel
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
                    Log.d(TAG, "*** MainViewModel NoteResult.Success")
                    viewStateLiveData.value =
                        MainViewState(notes = (t.data as? List<Note>))
                }
                is NoteResult.Error -> {
                    Log.d(TAG, "*** MainViewModel NoteResult.Error")
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