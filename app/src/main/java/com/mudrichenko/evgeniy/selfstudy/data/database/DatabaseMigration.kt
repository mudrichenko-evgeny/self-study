package com.mudrichenko.evgeniy.selfstudy.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuestionEntity
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizPackEntity
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizQuestionEntity

class DatabaseMigration {

    val migrations: Array<Migration> = arrayOf(
        object :  Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(QuestionEntity.DELETE_TABLE_STATEMENT)
                database.execSQL(QuestionEntity.CREATE_TABLE_STATEMENT)
                database.execSQL(QuizQuestionEntity.DELETE_TABLE_STATEMENT)
                database.execSQL(QuizQuestionEntity.CREATE_TABLE_STATEMENT)
                database.execSQL(QuizPackEntity.DELETE_TABLE_STATEMENT)
                database.execSQL(QuizPackEntity.CREATE_TABLE_STATEMENT)
            }
        }
    )

}