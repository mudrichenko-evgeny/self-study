package com.mudrichenkoevgeny.selfstudy.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuestionEntity
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuizPackEntity
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuizQuestionEntity

class DatabaseMigration {

    val migrations: Array<Migration> = arrayOf(
        object :  Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `${QuizPackEntity.TABLE_NAME}` ADD COLUMN `${QuizPackEntity.FILE_MD5}` TEXT NOT NULL DEFAULT ''")
            }
        }
    )

}