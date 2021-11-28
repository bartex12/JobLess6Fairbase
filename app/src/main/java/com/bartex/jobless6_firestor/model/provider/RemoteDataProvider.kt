package com.bartex.jobless6_firestor.model.provider

import androidx.lifecycle.LiveData
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult

//Так как запросы к базе данных асинхронные, нужен механизм отложенного получения результата.
//Будем использовать для этого LiveData. В качестве возвращаемого значения применим NoteResult. В
//последнем методе осмысленного значения возвращаться не будет, но при успешном сохранении
//записи будем возвращать ее — чтобы понять, что операция успешно завершилась
interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>

}