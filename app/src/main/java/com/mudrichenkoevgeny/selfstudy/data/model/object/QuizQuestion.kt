package com.mudrichenkoevgeny.selfstudy.data.model.`object`

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizQuestion(
    val questionId: Long = 0L,
    val order: Int = 1,
    val content: String,
    val correctAnswer: String,
    var userAnswerText: String?,
    var hasAudioRecord: Boolean,
    var answerStatus: AnswerStatus = AnswerStatus.NOT_ANSWERED
): Parcelable {

    fun isAnswered(): Boolean {
        return answerStatus != AnswerStatus.NOT_ANSWERED
    }

    fun isAnswerChecked(): Boolean {
        return answerStatus == AnswerStatus.WRONG || answerStatus == AnswerStatus.CORRECT
    }

    fun isAnswerCorrect(): Boolean {
        return answerStatus == AnswerStatus.CORRECT
    }

    fun getDisplayedUserAnswerText(): String {
        return userAnswerText ?: ""
    }

    fun hasAnswer(): Boolean {
        return true // temporary
//        return userAnswerText?.isNotEmpty() == true || hasAudioRecord
    }

    fun getAudioRecordName(): String {
        return "$questionId"
    }

}