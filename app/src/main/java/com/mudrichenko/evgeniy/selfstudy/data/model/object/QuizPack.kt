package com.mudrichenko.evgeniy.selfstudy.data.model.`object`

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizPack(
    val id: Long,
    val name: String,
    val fileName: String,
    var isUserPack: Boolean,
    var isActive: Boolean = false
): Parcelable {

    fun canBeRemoved(): Boolean {
        return isUserPack && !isActive
    }

    fun getBackupFileName(): String {
        return "$id"
    }

}