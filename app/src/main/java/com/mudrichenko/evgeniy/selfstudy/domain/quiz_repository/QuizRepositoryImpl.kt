package com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository

import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuestionDao
import com.mudrichenko.evgeniy.selfstudy.data.database.dao.QuizQuestionDao
import com.mudrichenko.evgeniy.selfstudy.data.database.type_converter.QuestionBackupTypeConverter
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.*
import com.mudrichenko.evgeniy.selfstudy.data.model.converter.*
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class QuizRepositoryImpl(
    private val questionDao: QuestionDao,
    private val quizQuestionDao: QuizQuestionDao,
    private val questionBackupTypeConverter: QuestionBackupTypeConverter
): QuizRepository {

    private val quizQuestionListFlow: MutableStateFlow<DataResponse<List<QuizQuestion>>> =
        MutableStateFlow(DataResponse.Successful(emptyList()))

    override suspend fun getQuizQuestionListFlow(): Flow<DataResponse<List<QuizQuestion>>> {
        return quizQuestionListFlow
    }

    private suspend fun emitQuizQuestionListFlow(quizQuestionList: DataResponse<List<QuizQuestion>>) {
        quizQuestionListFlow.emit(quizQuestionList)
    }

    override suspend fun syncData() {
        syncQuestionList()
        syncQuizQuestionList()
    }

    private suspend fun syncQuestionList() {
        emitQuestionListFlow(DataResponse.Successful(getQuestionListFromDatabase()))
    }

    private suspend fun syncQuizQuestionList() {
        emitQuizQuestionListFlow(getQuizQuestionList())
    }

    override suspend fun createQuiz(
        numberOfQuestions: Int,
        prioritizeDifficultQuestions: Boolean
    ): DataResponse<List<QuizQuestion>> {
        val questionEntityList = questionDao.getAll().let { questionEntityList ->

            val sortedQuestionEntityList = if (prioritizeDifficultQuestions)
                questionEntityList.sortedByDescending { it.getCorrectPercent() }
            else
                questionEntityList

            sortedQuestionEntityList.shuffled().take(
                if (numberOfQuestions > 0)
                    numberOfQuestions
                else
                    sortedQuestionEntityList.size
            )
        }

        val questionList: List<Question> = questionEntityList
            .map { resultedQuestionEntityList ->
                resultedQuestionEntityList.toModel()
            }

        val quizQuestionList = questionList.mapIndexed { index, question ->
            QuizQuestion(
                questionId = question.id,
                order = index + 1,
                content =  question.content,
                correctAnswer = question.answer,
                userAnswerText = null,
                hasAudioRecord = false
            )
        }
        saveQuizQuestionData(quizQuestionList)

        return if (quizQuestionList.isNotEmpty())
            DataResponse.Successful(quizQuestionList)
        else
            DataResponse.Error(AppError.NO_DATA)
    }

    private suspend fun saveQuizQuestionData(quizQuestionList: List<QuizQuestion>) {
        quizQuestionDao.clear()
        quizQuestionDao.insertAll(
            quizQuestionList.map { quizQuestion ->
                quizQuestion.toEntity()
            }
        )
    }

    override suspend fun finishQuiz(): DataResponse<List<QuizQuestion>> {
        quizQuestionDao.clear()
        syncData()
        return getQuizQuestionList()
    }

    override suspend fun getQuizQuestionList(): DataResponse<List<QuizQuestion>> {
        return DataResponse.Successful(
            quizQuestionDao.getAll()
                .sortedBy { it.order }
                .map { quizQuestionEntity ->
                    quizQuestionEntity.toModel()
                }
        )
    }

    override suspend fun getQuizQuestion(quizQuestionId: Long): DataResponse<QuizQuestion> {
        quizQuestionDao.getOne(quizQuestionId)?.toModel().let { quizQuestion ->
            return if (quizQuestion != null)
                DataResponse.Successful(quizQuestion)
            else
                DataResponse.Error(AppError.NO_DATA)
        }
    }

    override suspend fun answeredOnQuizQuestion(
        quizQuestion: QuizQuestion
    ): DataResponse<QuizQuestion> {
        return editQuizQuestion(quizQuestion)
    }

    override suspend fun quizQuestionChecked(
        quizQuestion: QuizQuestion
    ): DataResponse<QuizQuestion> {
        saveQuestionStatistic(quizQuestion)
        return editQuizQuestion(quizQuestion)
    }

    private suspend fun editQuizQuestion(quizQuestion: QuizQuestion): DataResponse<QuizQuestion> {
        quizQuestionDao.getOne(
            quizQuestionDao.insert(quizQuestion.toEntity())
        )?.toModel().let { savedQuizQuestion ->
            syncData()
            return if (savedQuizQuestion != null)
                DataResponse.Successful(savedQuizQuestion)
            else
                DataResponse.Error(AppError.UNKNOWN)
        }
    }


    private suspend fun saveQuestionStatistic(quizQuestion: QuizQuestion) {
        questionDao.getOne(quizQuestion.questionId)?.let { questionEntity ->
            questionEntity.attemptCount = questionEntity.attemptCount + 1
            if (quizQuestion.isAnswerCorrect()) {
                questionEntity.correctCount = questionEntity.correctCount + 1
            }
            questionDao.insert(questionEntity)
        }
    }

    private val questionListFlow: MutableStateFlow<DataResponse<List<Question>>> =
        MutableStateFlow(DataResponse.Successful(emptyList()))

    override suspend fun getQuestionListFlow(): Flow<DataResponse<List<Question>>> {
        return questionListFlow
    }

    private suspend fun emitQuestionListFlow(questionList: DataResponse<List<Question>>) {
        questionListFlow.emit(questionList)
    }

    private suspend fun getQuestionListFromDatabase(): List<Question> {
        return questionDao.getAll().map { questionEntity ->
            questionEntity.toModel()
        }
    }

    override suspend fun getQuestionBackupText(): DataResponse<String> {
        val backupList = questionDao.getAll().map { questionEntity ->
            QuestionBackup(
                id = questionEntity.id,
                attemptCount = questionEntity.attemptCount,
                correctCount = questionEntity.correctCount
            )
        }
        questionBackupTypeConverter.typeToString(backupList).let { backupText ->
            return if (backupText != null) {
                DataResponse.Successful(backupText)
            } else {
                DataResponse.Error(AppError.NO_DATA)
            }
        }
    }

    override suspend fun setQuestionFromBackup(backupText: String): DataResponse<Unit> {
        val backupList = questionBackupTypeConverter.stringToType(backupText)
        val questionEntityList = questionDao.getAll().map { questionEntity ->
            val question = backupList.find { questionBackup ->
                questionBackup.id == questionEntity.id
            }
            if (question != null) {
                QuestionEntity().apply {
                    this.id = questionEntity.id
                    this.content = questionEntity.content
                    this.answer = questionEntity.answer
                    this.attemptCount = question.attemptCount
                    this.correctCount = question.correctCount
                }
            } else {
                questionEntity
            }
        }
        questionDao.insertAll(questionEntityList)
        syncQuestionList()
        return DataResponse.Successful(Unit)
    }

    override suspend fun createDatabaseFromRows(
        quizPackId: Long?,
        rows: List<Map<String, String>>
    ): DataResponse<List<Question>> {
        val fileQuestionsList = rows.mapNotNull { row ->
            getQuestion(row)
        }
        return if (fileQuestionsList.isEmpty()) {
            DataResponse.Error(AppError.QUESTION_PACK_CREATE)
        } else {
            questionDao.clear()
            questionDao.insertAll(fileQuestionsList.mapIndexed { index, question ->
                question.toEntity().apply {
                    this.id = index.toLong()
                }
            })
            questionDao.getAll().map { it.toModel() }.let { questionsList ->
                val response = DataResponse.Successful(questionsList)
                emitQuestionListFlow(response)
                response
            }
        }
    }

    override suspend fun getQuizPackTable(): List<List<String>> {
        val quizPackTable: MutableList<List<String>> = mutableListOf()
        quizPackTable.add(
            listOf(
                QuizRepository.COLUMN_KEY_QUESTION,
                QuizRepository.COLUMN_KEY_ANSWER
            )
        )
        questionDao.getAll().map { questionEntity ->
            quizPackTable.add(
                listOf(
                    questionEntity.content,
                    questionEntity.answer
                )
            )
        }
        return quizPackTable
    }

    private fun getQuestion(row: Map<String, String>): Question? {
        val question = row.getOrDefault(QuizRepository.COLUMN_KEY_QUESTION, "")
        val answer = row.getOrDefault(QuizRepository.COLUMN_KEY_ANSWER, "")
        return if (question.isNotEmpty()) {
            Question(
                id = 0L,
                content = question,
                answer = answer,
                attemptCount = 0,
                correctCount = 0
            )
        } else {
            null
        }
    }

    override suspend fun editQuestion(
        question: Question,
        content: String,
        answer: String
    ): DataResponse<Question> {
        val modifiedQuestion = Question(
            id = question.id,
            content = content,
            answer = answer,
            attemptCount = question.attemptCount,
            correctCount = question.correctCount
        )
        questionDao.insert(modifiedQuestion.toEntity())
        return DataResponse.Successful(modifiedQuestion)
    }

    override suspend fun deleteQuestion(questionId: Long): DataResponse<Long> {
        questionDao.deleteOne(questionId)
        syncQuestionList()
        return DataResponse.Successful(questionId)
    }

    override suspend fun getQuestion(questionId: Long): DataResponse<Question> {
        return questionDao.getOne(questionId)?.toModel().let { question ->
            if (question != null) {
                DataResponse.Successful(question)
            } else {
                DataResponse.Error(AppError.NO_DATA)
            }
        }
    }

    override suspend fun hasQuestionPack(): DataResponse<Boolean> {
        return DataResponse.Successful(getQuestionListFromDatabase().isNotEmpty())
    }

    override suspend fun clearStatistics(): DataResponse<List<Question>> {
        questionDao.getAll().map { questionEntity ->
            questionEntity.attemptCount = 0
            questionEntity.correctCount = 0
            questionEntity
        }.let { questionEntityList ->
            questionDao.clear()
            questionDao.insertAll(questionEntityList)
            syncQuestionList()
            return DataResponse.Successful(
                questionEntityList.map { questionEntity ->
                    questionEntity.toModel()
                }
            )
        }
    }

}