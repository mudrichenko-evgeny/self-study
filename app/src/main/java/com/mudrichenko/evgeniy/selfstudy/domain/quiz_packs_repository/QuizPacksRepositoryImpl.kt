package com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository

import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuizPacksDao
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AppError
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import com.mudrichenko.evgeniy.selfstudy.data.model.converter.toModel
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizPackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class QuizPacksRepositoryImpl(
    private val quizPacksDao: QuizPacksDao
): QuizPacksRepository {

    private val assetPackNamesList: Map<String, String> = mapOf(
        Pair("general_knowledge_en.csv", "General Knowledge"),
        Pair("tongue_twisters_en.csv", "Tongue Twisters")
    )

    override suspend fun syncData() {
        syncQuizPacksData()
    }

    private val quizPacksListFlow: MutableStateFlow<DataResponse<List<QuizPack>>> =
        MutableStateFlow(DataResponse.Successful(emptyList()))

    override suspend fun getQuizPacksListFlow(): Flow<DataResponse<List<QuizPack>>> {
        return quizPacksListFlow
    }

    private suspend fun emitQuizPacksListFlow(quizPacksList: DataResponse<List<QuizPack>>) {
        quizPacksListFlow.emit(quizPacksList)
    }

    private val activeQuizPackFlow: MutableStateFlow<QuizPack?> =
        MutableStateFlow(null)

    override suspend fun getActiveQuizPackFlow(): Flow<QuizPack?> {
        return activeQuizPackFlow
    }

    private suspend fun emitActiveQuizPackFlow(quizPack: QuizPack?) {
        activeQuizPackFlow.emit(quizPack)
    }

    private suspend fun syncQuizPacksData() {
        emitQuizPacksListFlow(getQuizPacksList())
        emitActiveQuizPackFlow(getActiveQuizPack())
    }

    private suspend fun getQuizPacksList(): DataResponse<List<QuizPack>> {
        return DataResponse.Successful(
            quizPacksDao.getAll().map { quizPackEntity ->
                quizPackEntity.toModel()
            }
        )
    }

    override suspend fun getActiveQuizPack(): QuizPack? {
        return quizPacksDao.getByActive(true).firstOrNull()?.toModel()
    }

    override suspend fun getPacksToCreate(fileNames: List<String>): DataResponse<List<String>> {
        val packsToCreate: MutableList<String> = mutableListOf()
        val quizPacksList: List<QuizPackEntity> = quizPacksDao.getAll()
        fileNames.map { fileName ->
            assetPackNamesList[fileName]?.let { packName ->
                val quizPack = quizPacksList.find { quizPack -> quizPack.name == packName }
                if (quizPack == null) {
                    packsToCreate.add(fileName)
                }
            }
        }
        return DataResponse.Successful(packsToCreate)
    }

    override suspend fun saveAssetQuizPacks(packFileNames: List<String>) {
        val quizPacksFromDatabase = quizPacksDao.getByIsUserPack(false)
        packFileNames.map { packFileName ->
            val quizPackFromDatabase = quizPacksFromDatabase.find { it.fileName == packFileName }
            if (quizPackFromDatabase == null) {
                quizPacksDao.insert(
                    QuizPackEntity().apply {
                        this.name = assetPackNamesList[packFileName]
                            ?: packFileName.replace(".csv", "")
                        this.fileName = packFileName
                        this.isUserPack = false
                        this.isActive = false
                    }
                )
                syncQuizPacksData()
            }
        }
    }

    override suspend fun saveQuizPack(packName: String, packFileName: String): DataResponse<Long> {
        val packId = quizPacksDao.insert(
            QuizPackEntity().apply {
                this.name = packName
                this.fileName = packFileName
                this.isUserPack = true
                this.isActive = false
            }
        )
        syncQuizPacksData()
        return DataResponse.Successful(packId)
    }

    override suspend fun deleteQuizPack(quizPack: QuizPack): DataResponse<Unit> {
        quizPacksDao.deleteOne(quizPack.id)
        syncQuizPacksData()
        return DataResponse.Successful(Unit)
    }

    override suspend fun getAnyQuizPack(): DataResponse<QuizPack> {
        quizPacksDao.getAll().firstOrNull().let { quizPackEntity ->
            return if (quizPackEntity != null)
                DataResponse.Successful(quizPackEntity.toModel())
            else
                DataResponse.Error(AppError.NO_DATA)
        }
    }

    override suspend fun setActiveQuizPack(quizPack: QuizPack) {
        val quizPacksList = quizPacksDao.getAll().map { quizPackEntity ->
            quizPackEntity.isActive = quizPackEntity.id == quizPack.id
            quizPackEntity
        }
        quizPacksDao.insertAll(quizPacksList)
        syncQuizPacksData()
    }

    override suspend fun getQuizPack(quizPackId: Long): DataResponse<QuizPack> {
        quizPacksDao.getOne(quizPackId)?.toModel().let { quizPack ->
            return if (quizPack != null) {
                DataResponse.Successful(quizPack)
            } else {
                DataResponse.Error(AppError.NO_DATA)
            }
        }
    }

    override suspend fun getAllQuizPacks(): DataResponse<List<QuizPack>> {
        val quizPacksList = quizPacksDao.getAll().map { quizPackEntity ->
            quizPackEntity.toModel()
        }
        return if (quizPacksList.isNotEmpty())
            DataResponse.Successful(quizPacksList)
        else
            DataResponse.Error(AppError.NO_DATA)
    }

    override suspend fun renameActiveQuizPack(name: String): DataResponse<QuizPack> {
        val quizPackEntity = quizPacksDao.getByActive(true)
            .firstOrNull() ?: return DataResponse.Error(AppError.QUESTION_PACK_EDIT)
        quizPackEntity.name = name
        quizPacksDao.update(quizPackEntity)
        syncQuizPacksData()
        return DataResponse.Successful(quizPackEntity.toModel())
    }

}