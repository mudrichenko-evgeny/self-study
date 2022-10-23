package com.mudrichenko.evgeniy.selfstudy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizQuestionEntity

@Dao
interface QuizQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuizQuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<QuizQuestionEntity>): List<Long>

    @Query("SELECT * FROM ${QuizQuestionEntity.TABLE_NAME}")
    suspend fun getAll(): List<QuizQuestionEntity>

    @Query("SELECT * FROM ${QuizQuestionEntity.TABLE_NAME} WHERE ${QuizQuestionEntity.QUESTION_ID} = :id")
    suspend fun getOne(id: Long): QuizQuestionEntity?

    @Query("DELETE FROM ${QuizQuestionEntity.TABLE_NAME}")
    suspend fun clear()

}