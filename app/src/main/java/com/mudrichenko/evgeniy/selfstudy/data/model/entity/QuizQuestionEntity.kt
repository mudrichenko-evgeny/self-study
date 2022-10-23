package com.mudrichenko.evgeniy.selfstudy.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AnswerStatus

@Entity(tableName = QuizQuestionEntity.TABLE_NAME)
class QuizQuestionEntity {

    @PrimaryKey
    @ColumnInfo(name = QUESTION_ID)
    var questionId: Long = 0L

    @ColumnInfo(name = ORDER)
    var order: Int = 1

    @ColumnInfo(name = CONTENT)
    var content: String = ""

    @ColumnInfo(name = CORRECT_ANSWER)
    var correctAnswer: String = ""

    @ColumnInfo(name = USER_ANSWER_TEXT)
    var userAnswerText: String? = null

    @ColumnInfo(name = HAS_AUDIO_RECORD)
    var hasAudioRecord: Boolean? = null

    @ColumnInfo(name = ANSWER_STATUS)
    var answerStatus: AnswerStatus = AnswerStatus.NOT_ANSWERED

    companion object {
        const val TABLE_NAME = "quizQuestions"

        const val QUESTION_ID = "questionId"
        const val ORDER = "order"
        const val CONTENT = "content"
        const val CORRECT_ANSWER = "correctAnswer"
        const val USER_ANSWER_TEXT = "userAnswerText"
        const val HAS_AUDIO_RECORD = "hasAudioRecord"
        const val ANSWER_STATUS = "answerStatus"

        const val CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (" +
                " `${QUESTION_ID}` INTEGER NOT NULL," +
                " `${ORDER}` INTEGER NOT NULL," +
                " `${CONTENT}` TEXT NOT NULL," +
                " `${CORRECT_ANSWER}` TEXT NOT NULL," +
                " `${USER_ANSWER_TEXT}` TEXT," +
                " `${HAS_AUDIO_RECORD}` INTEGER," +
                " `${ANSWER_STATUS}` TEXT," +
                " PRIMARY KEY(`${QUESTION_ID}`))"
        const val DELETE_TABLE_STATEMENT = "DROP TABLE IF EXISTS `${TABLE_NAME}`"
    }

}