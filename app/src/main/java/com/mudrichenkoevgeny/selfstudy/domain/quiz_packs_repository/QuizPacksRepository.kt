package com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository

import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import kotlinx.coroutines.flow.Flow

interface QuizPacksRepository {

    suspend fun syncData()
    suspend fun getQuizPacksListFlow(): Flow<DataResponse<List<QuizPack>>>
    suspend fun getActiveQuizPackFlow(): Flow<QuizPack?>
    suspend fun getActiveQuizPack(): QuizPack?
    suspend fun getDefaultQuizPacks(): List<QuizPack>
    suspend fun saveQuizPack(
        packName: String,
        packFileName: String,
        isUserPack: Boolean,
        packFileMD5: String
    ): DataResponse<Long>
    suspend fun deleteQuizPack(quizPack: QuizPack): DataResponse<Unit>
    suspend fun getAnyQuizPack(): DataResponse<QuizPack>

    suspend fun setActiveQuizPack(quizPack: QuizPack)
    suspend fun getQuizPack(quizPackId: Long): DataResponse<QuizPack>
    suspend fun getAllQuizPacks(): DataResponse<List<QuizPack>>
    suspend fun renameActiveQuizPack(name: String): DataResponse<QuizPack>

}