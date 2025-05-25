package com.mudrichenkoevgeny.selfstudy.data.model.`object`

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizPack(
    val id: Long,
    val name: String,
    val fileName: String,
    val fileMD5: String,
    var isUserPack: Boolean,
    var isActive: Boolean = false
): Parcelable {

    fun canBeRemoved(): Boolean {
        return !isActive
    }

    fun getBackupFileName(): String {
        return "$id"
    }

}