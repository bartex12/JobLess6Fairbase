package com.bartex.jobless6_firestor.view.note

import com.bartex.jobless6_firestor.model.Note

//мы изменили способ передачи заметки в NoteActivity. Теперь будем передавать туда
// только id заметки, а ее экземпляр получать из базы данных. Для этого понадобится
// контейнер — будем использовать NoteViewState для передачи данных из ViewModel в Activity.

//В поле data значение будет инициализировано типом Note?. Чтобы было можно передать
// NoteViewState без данных, мы сделали этот тип поддерживающим значение null.
class NoteViewState(val note: Note? = null, val error:Throwable? = null)