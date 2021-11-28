package com.bartex.jobless6_firestor.view.main

import com.bartex.jobless6_firestor.model.Note

//Параметризуем MainViewState List<Note>?. Таким образом можем передавать null, когда данных нет.
// В конструкторе MainViewState зададим значения по умолчанию для данных и ошибки равными null.
// Теперь можем создать экземпляр MainViewState, не передавая в конструктор ничего.
// Таким образом можно делать пустой конструктор по умолчанию — для классов,
// у которых не пустые конструкторы
class MainViewState(val notes: List<Note>? = null, val error: Throwable? = null)
