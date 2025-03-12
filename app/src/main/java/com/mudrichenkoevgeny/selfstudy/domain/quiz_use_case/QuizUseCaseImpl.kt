package com.mudrichenkoevgeny.selfstudy.domain.quiz_use_case

import android.util.Log
import com.mudrichenkoevgeny.selfstudy.data.UniqueEvent
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.AppError
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenkoevgeny.selfstudy.domain.file_repository.FileRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class QuizUseCaseImpl(
    private val fileRepository: FileRepository,
    private val quizRepository: QuizRepository,
    private val quizPacksRepository: QuizPacksRepository
): QuizUseCase {

    private val resetQuizEventFlow: MutableSharedFlow<UniqueEvent?> = MutableSharedFlow()

    override suspend fun resetQuizEventFlow(): MutableSharedFlow<UniqueEvent?> {
        return resetQuizEventFlow
    }

    private suspend fun emitResetQuizEvent() {
        resetQuizEventFlow.emit(UniqueEvent())
    }

    override suspend fun checkPacks() {
        createPacksFromAsset()
        checkQuizPack()
    }

    override suspend fun setActiveQuiz(quizPack: QuizPack): DataResponse<QuizPack> {
        backupActivePackData()
        return createBlankQuizPackData(quizPack)
    }

    override suspend fun deleteQuiz(quizPack: QuizPack): DataResponse<Unit> {
        deleteQuizPackFromDatabase(quizPack)
        deleteQuizBackupFile(quizPack)
        deleteQuizPackFile(quizPack)
        return DataResponse.Successful(Unit)
    }

    override suspend fun editQuiz(quizPack: QuizPack): DataResponse<QuizPack> {
        editPackFile(quizPack)
        return createFromPackFile(quizPack)
    }

    private suspend fun createPacksFromAsset() {
        val packFileNameList: List<String> = fileRepository.getDefaultQuizPackNames()
        val packsToCreateResponse: DataResponse<List<String>> =
            quizPacksRepository.getPacksToCreate(packFileNameList)
        if (packsToCreateResponse is DataResponse.Successful) {
            val successfullyCreatedPacks: MutableList<String> = mutableListOf()
            packsToCreateResponse.data.map { packFileName ->
                fileRepository.createQuizPackFromAssets(
                    fileName = packFileName
                )?.let {
                    successfullyCreatedPacks.add(packFileName)
                }
            }
            quizPacksRepository.saveAssetQuizPacks(successfullyCreatedPacks)
        }
    }

    private suspend fun checkQuizPack() {
        quizRepository.hasQuestionPack().let { dataResponse ->
            if (dataResponse !is DataResponse.Successful || !dataResponse.data) {
                createDefaultQuiz()
            }
        }
    }

    private suspend fun createDefaultQuiz() {
        quizPacksRepository.getAnyQuizPack().let { quizPackResponse ->
            if (quizPackResponse is DataResponse.Successful) {
                setActiveQuiz(quizPackResponse.data)
            }
        }
    }

    private suspend fun editPackFile(quizPack: QuizPack) {
        fileRepository.saveRowsIntoCsv(
            file = fileRepository.getFile(
                directory = fileRepository.getPacksDirectory(quizPack),
                fileName = quizPack.fileName
            ),
            rows = quizRepository.getQuizPackTable()
        )
    }

    private suspend fun createFromPackFile(quizPack: QuizPack): DataResponse<QuizPack> {
        createQuizDatabaseFromQuizPackFile(quizPack).let { response ->
            resetQuiz()
            return response
        }
    }

    private suspend fun backupActivePackData() {
        quizPacksRepository.getActiveQuizPack().let { activeQuizPack ->
            if (activeQuizPack != null) {
                quizRepository.getQuestionBackupText().data()?.let { backupText ->
                    fileRepository.writeTextToFile(
                        directory = fileRepository.getBackupDirectory(),
                        fileName = activeQuizPack.getBackupFileName(),
                        text = backupText
                    )
                }
            }
        }
    }

    private suspend fun createBlankQuizPackData(
        quizPack: QuizPack
    ): DataResponse<QuizPack> {
        createQuizDatabaseFromQuizPackFile(quizPack).let { response ->
            if (response is DataResponse.Error) return response
        }
        fileRepository.readTextFromFile(
            directory = fileRepository.getBackupDirectory(),
            fileName = quizPack.getBackupFileName()
        )?.let { backupText ->
            quizRepository.setQuestionFromBackup(backupText)
        }
        quizPacksRepository.setActiveQuizPack(quizPack)
        resetQuiz()
        return DataResponse.Successful(quizPack)
    }

    private suspend fun createQuizDatabaseFromQuizPackFile(
        quizPack: QuizPack
    ): DataResponse<QuizPack> {
        val quizPackFile = fileRepository.getFile(
            directory = fileRepository.getPacksDirectory(quizPack),
            fileName = quizPack.fileName
        ) ?: return DataResponse.Error(AppError.NO_FILE)

        val rows = fileRepository.getRowsFromCsvFile(
            file = quizPackFile
        )
        if (rows.isEmpty()) return DataResponse.Error(AppError.FILE_CONVERT)

        val createDatabaseResponse = quizRepository.createDatabaseFromRows(
            quizPackId = quizPack.id,
            rows = rows
        )
        if (createDatabaseResponse is DataResponse.Error) {
            return DataResponse.Error(createDatabaseResponse.appError)
        }

        return DataResponse.Successful(quizPack)
    }

    private fun deleteQuizBackupFile(quizPack: QuizPack) {
        fileRepository.deleteFile(
            directory = fileRepository.getBackupDirectory(),
            fileName = quizPack.getBackupFileName()
        )
    }

    private fun deleteQuizPackFile(quizPack: QuizPack) {
        fileRepository.deleteFile(
            directory = fileRepository.getPacksDirectory(quizPack),
            fileName = quizPack.fileName
        )
    }

    private suspend fun deleteQuizPackFromDatabase(quizPack: QuizPack) {
        quizPacksRepository.deleteQuizPack(quizPack)
    }

    private suspend fun resetQuiz() {
        quizRepository.finishQuiz()
        deleteAudioRecords()
        emitResetQuizEvent()
    }

    override suspend fun createQuiz(
        numberOfQuestion: Int,
        prioritizeDifficultQuestions: Boolean
    ): DataResponse<List<QuizQuestion>> {
        Log.d("HOME_LOGS", "prioritizeDifficultQuestions = $prioritizeDifficultQuestions")
        deleteAudioRecords()
        return quizRepository.createQuiz(
            numberOfQuestions = numberOfQuestion,
            prioritizeDifficultQuestions = prioritizeDifficultQuestions
        )
    }

    private fun deleteAudioRecords() {
        fileRepository.deleteDirectory(fileRepository.getAudioRecordsDirectory())
    }

}