package com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import kotlinx.coroutines.flow.Flow

interface QuizPacksRepository {

    suspend fun syncData()
    suspend fun getQuizPacksListFlow(): Flow<DataResponse<List<QuizPack>>>
    suspend fun getActiveQuizPackFlow(): Flow<QuizPack?>
    suspend fun getActiveQuizPack(): QuizPack?

    suspend fun getPacksToCreate(fileNames: List<String>): DataResponse<List<String>>
    suspend fun saveAssetQuizPacks(packFileNames: List<String>)
    suspend fun saveQuizPack(packName: String, packFileName: String): DataResponse<Long>
    suspend fun deleteQuizPack(quizPack: QuizPack): DataResponse<Unit>
    suspend fun getAnyQuizPack(): DataResponse<QuizPack>

    suspend fun setActiveQuizPack(quizPack: QuizPack)
    suspend fun getQuizPack(quizPackId: Long): DataResponse<QuizPack>
    suspend fun getAllQuizPacks(): DataResponse<List<QuizPack>>
    suspend fun renameActiveQuizPack(name: String): DataResponse<QuizPack>

}