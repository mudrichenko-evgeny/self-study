package com.mudrichenko.evgeniy.selfstudy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuestionDao
import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuizPacksDao
import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuizQuestionDao
import com.mudrichenko.evgeniy.selfstudy.data.database.type_converter.AnswerStatusTypeConverter
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuestionEntity
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizPackEntity
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizQuestionEntity

@Database(
    entities = [
        QuestionEntity::class,
        QuizQuestionEntity::class,
        QuizPackEntity::class
    ],
    version = AppDatabase.DB_VERSION
)
@TypeConverters(
    AnswerStatusTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "database"
        const val DB_VERSION = 1
    }

    abstract fun questionDao(): QuestionDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun quizPacksDao(): QuizPacksDao

}
