package com.bartex.jobless6_firestor.model

sealed class NoteResult{
    data class Success<out T>(val data: T) : NoteResult() //чтение
    data class Error(val error: Throwable) : NoteResult()
}



