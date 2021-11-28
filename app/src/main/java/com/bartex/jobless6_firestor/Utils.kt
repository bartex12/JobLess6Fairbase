package com.bartex.jobless6_firestor

import com.bartex.jobless6_firestor.model.Note
import java.util.*

object Utils {

    fun getDate(): String {
        val calendar = GregorianCalendar()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return String.format("%02d-%02d-%04d", day, month+1, year)
    }

    fun getTime(): String {
        val calendar = GregorianCalendar()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    fun sortListOfNoteDescending(data: List<Note>?): List<Note>? {
        return data?.sortedByDescending {
            val date: List<String> = (it.titleDate).split("-")
            val time: List<String> = (it.lastTime).split(":")
            GregorianCalendar(
                date[2].toInt(), date[1].toInt(), date[0].toInt(),
                time[0].toInt(), time[1].toInt()
            ).time
        }
    }
}