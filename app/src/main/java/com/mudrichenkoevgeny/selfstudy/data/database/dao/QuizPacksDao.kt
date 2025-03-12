package com.mudrichenkoevgeny.selfstudy.data.database.dao

import androidx.room.*
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuizPackEntity

@Dao
interface QuizPacksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuizPackEntity): Long

    @Update
    suspend fun update(entity: QuizPackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<QuizPackEntity>): List<Long>

    @Query("SELECT * FROM ${QuizPackEntity.TABLE_NAME}")
    suspend fun getAll(): List<QuizPackEntity>

    @Query("SELECT * FROM ${QuizPackEntity.TABLE_NAME} WHERE ${QuizPackEntity.ID} = :id")
    suspend fun getOne(id: Long): QuizPackEntity?

    @Query("SELECT * FROM ${QuizPackEntity.TABLE_NAME} WHERE ${QuizPackEntity.IS_ACTIVE} = :isActive")
    suspend fun getByActive(isActive: Boolean): List<QuizPackEntity>

    @Query("SELECT * FROM ${QuizPackEntity.TABLE_NAME} WHERE ${QuizPackEntity.IS_USER_PACK} = :isUserPack")
    suspend fun getByIsUserPack(isUserPack: Boolean): List<QuizPackEntity>

    @Query("DELETE FROM ${QuizPackEntity.TABLE_NAME} WHERE ${QuizPackEntity.ID} = :id")
    suspend fun deleteOne(id: Long)

    @Query("DELETE FROM ${QuizPackEntity.TABLE_NAME}")
    suspend fun clear()

}