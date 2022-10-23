package com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.Question
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface QuizRepository {

    suspend fun syncData()
    suspend fun getQuizQuestionListFlow(): Flow<DataResponse<List<QuizQuestion>>>
    suspend fun createQuiz(
        numberOfQuestions: Int,
        prioritizeDifficultQuestions: Boolean
    ): DataResponse<List<QuizQuestion>>
    suspend fun finishQuiz(): DataResponse<List<QuizQuestion>>
    suspend fun getQuizQuestionList(): DataResponse<List<QuizQuestion>>
    suspend fun getQuizQuestion(quizQuestionId: Long): DataResponse<QuizQuestion>
    suspend fun answeredOnQuizQuestion(quizQuestion: QuizQuestion): DataResponse<QuizQuestion>
    suspend fun quizQuestionChecked(quizQuestion: QuizQuestion): DataResponse<QuizQuestion>

    suspend fun getQuestionListFlow(): Flow<DataResponse<List<Question>>>

    suspend fun getQuestionBackupText(): DataResponse<String>
    suspend fun setQuestionFromBackup(backupText: String): DataResponse<Unit>

    suspend fun createDatabaseFromRows(
        quizPackId: Long?,
        rows: List<Map<String, String>>
    ): DataResponse<List<Question>>
    suspend fun getQuizPackTable(): List<List<String>>

    suspend fun editQuestion(
        question: Question,
        content: String,
        answer: String
    ): DataResponse<Question>
    suspend fun deleteQuestion(questionId: Long): DataResponse<Long>
    suspend fun getQuestion(questionId: Long): DataResponse<Question>
    suspend fun hasQuestionPack(): DataResponse<Boolean>
    suspend fun clearStatistics(): DataResponse<List<Question>>

    companion object {
        const val COLUMN_KEY_QUESTION = "question"
        const val COLUMN_KEY_ANSWER = "answer"
    }

}