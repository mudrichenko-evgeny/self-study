package com.mudrichenko.evgeniy.selfstudy.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = QuizPackEntity.TABLE_NAME)
class QuizPackEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long = 0L

    @ColumnInfo(name = NAME)
    var name: String = ""

    @ColumnInfo(name = FILE_NAME)
    var fileName: String = ""

    @ColumnInfo(name = IS_USER_PACK)
    var isUserPack: Boolean = false

    @ColumnInfo(name = IS_ACTIVE)
    var isActive: Boolean = false

    companion object {
        const val TABLE_NAME = "quizPacks"

        const val ID = "id"
        const val NAME = "name"
        const val FILE_NAME = "fileName"
        const val IS_USER_PACK = "isUserPack"
        const val IS_ACTIVE = "isActive"

        const val CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (" +
                "`${ID}` INTEGER NOT NULL," +
                " `${NAME}` TEXT NOT NULL," +
                " `${FILE_NAME}` TEXT NOT NULL," +
                " `${IS_USER_PACK}` INTEGER NOT NULL," +
                " `${IS_ACTIVE}` INTEGER NOT NULL," +
                " PRIMARY KEY(`${ID}`))"
        const val DELETE_TABLE_STATEMENT = "DROP TABLE IF EXISTS `${TABLE_NAME}`"
    }

}