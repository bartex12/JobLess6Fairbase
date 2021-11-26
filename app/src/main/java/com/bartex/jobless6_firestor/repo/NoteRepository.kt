package com.bartex.jobless6_firestor.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bartex.jobless6_firestor.model.Note
import java.util.*

object NoteRepository {

    private val  notesLiveData: MutableLiveData<List<Note>> = MutableLiveData()

    private   val notes:MutableList<Note> = mutableListOf(
        Note(
            id =UUID.randomUUID().toString(),
            titleDate="24-05-2021",
            highPress ="123",
            lowPress = "85",
            pulse = "70",
            lastTime = "7:25"
        ),
        Note(
            id =UUID.randomUUID().toString(),
            titleDate="24-05-2021",
            highPress ="128",
            lowPress = "85",
            pulse = "75",
            lastTime = "17:25"
        ),
        Note(
            id =UUID.randomUUID().toString(),
            titleDate="25-05-2021",
            highPress ="145",
            lowPress = "90",
            pulse = "80",
            lastTime = "7:35"
        ),
        Note(
            id =UUID.randomUUID().toString(),
            titleDate="25-05-2021",
            highPress ="150",
            lowPress = "85",
            pulse = "85",
            lastTime = "17:25"
        ),
        Note(
            id =UUID.randomUUID().toString(),
            titleDate="25-05-2021",
            highPress ="123",
            lowPress = "85",
            pulse = "72",
            lastTime = "22:25"
        )
    )

    //init блок идёт после объявления и инициализации переменных, которые в нём участвуют
    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note){
        //добавляем в список или обновляем заметку в списке
        addOrReplace(note)
        //меняем список в LiveData
        notesLiveData.value = notes
    }

    //сравниваем заметки с помощью знака ==, в Kotlin это означает вызов метода equals()
    //у нас этот метод переопределен и сравнение идет по полю id
    private  fun addOrReplace(note: Note){
        //если в списке уже есть такая заметка - обновляем её
        for (i in notes.indices){
            if(notes[i] == note){ //если id совпадает
                notes[i] =  note
                return
            }
        }
        //если после прочёсывания списка нет совпадений, добавляем заметку в список
        notes.add(note)
    }

    fun getNote(): LiveData<List<Note>> {
        return notesLiveData
    }

}