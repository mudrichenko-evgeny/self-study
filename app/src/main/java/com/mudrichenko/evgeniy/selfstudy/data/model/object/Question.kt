package com.mudrichenko.evgeniy.selfstudy.data.model.`object`

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: Long,
    val content: String,
    val answer: String,
    var attemptCount: Int,
    var correctCount: Int
): Parcelable {

    fun getCorrectPercent(): Float {
        return correctCount.toFloat()/attemptCount.toFloat()
    }

    fun getStatisticText(): String {
        return "$correctCount/$attemptCount"
    }

}