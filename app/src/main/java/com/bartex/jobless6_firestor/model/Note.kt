package com.bartex.jobless6_firestor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id:String ="",
    val titleDate:String = "",
    val highPress:String = "",
    val lowPress:String = "",
    val pulse:String = "",
    val lastTime:String = ""
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(this.javaClass !=  other?.javaClass) return false
        other as Note
        if(id !=other.id) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + titleDate.hashCode()
        result = 31 * result + highPress.hashCode()
        result = 31 * result + lowPress.hashCode()
        result = 31 * result + pulse.hashCode()
        result = 31 * result + lastTime.hashCode()
        return result
    }


}