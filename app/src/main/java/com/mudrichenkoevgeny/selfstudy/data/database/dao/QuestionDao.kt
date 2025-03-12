package com.mudrichenkoevgeny.selfstudy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuestionEntity

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<QuestionEntity>): List<Long>

    @Query("SELECT * FROM ${QuestionEntity.TABLE_NAME}")
    suspend fun getAll(): List<QuestionEntity>

    @Query("SELECT * FROM ${QuestionEntity.TABLE_NAME} WHERE ${QuestionEntity.ID} = :id")
    suspend fun getOne(id: Long): QuestionEntity?

    @Query("DELETE FROM ${QuestionEntity.TABLE_NAME} WHERE ${QuestionEntity.ID} = :id")
    suspend fun deleteOne(id: Long)

    @Query("DELETE FROM ${QuestionEntity.TABLE_NAME}")
    suspend fun clear()

}