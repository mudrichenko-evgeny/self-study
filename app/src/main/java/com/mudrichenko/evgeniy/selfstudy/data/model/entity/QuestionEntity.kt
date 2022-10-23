package com.mudrichenko.evgeniy.selfstudy.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = QuestionEntity.TABLE_NAME)
class QuestionEntity {

    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: Long = 0L

    @ColumnInfo(name = CONTENT)
    var content: String = ""

    @ColumnInfo(name = ANSWER)
    var answer: String = ""

    @ColumnInfo(name = ATTEMPT_COUNT)
    var attemptCount: Int = 0

    @ColumnInfo(name = CORRECT_COUNT)
    var correctCount: Int = 0

    fun getCorrectPercent(): Float {
        return correctCount.toFloat()/attemptCount.toFloat()
    }

    companion object {
        const val TABLE_NAME = "questions"

        const val ID = "id"
        const val CONTENT = "content"
        const val ANSWER = "answer"
        const val ATTEMPT_COUNT = "attempt_count"
        const val CORRECT_COUNT = "correct_count"

        const val CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (" +
                "`${ID}` INTEGER NOT NULL," +
                " `${CONTENT}` TEXT NOT NULL," +
                " `${ANSWER}` TEXT NOT NULL," +
                "`${ATTEMPT_COUNT}` INTEGER NOT NULL," +
                "`${CORRECT_COUNT}` INTEGER NOT NULL," +
                " PRIMARY KEY(`${ID}`))"
        const val DELETE_TABLE_STATEMENT = "DROP TABLE IF EXISTS `${TABLE_NAME}`"
    }

}