package com.mudrichenko.evgeniy.selfstudy.domain.quiz_use_case

import com.mudrichenko.evgeniy.selfstudy.data.UniqueEvent
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import kotlinx.coroutines.flow.MutableSharedFlow

interface QuizUseCase {
    suspend fun resetQuizEventFlow(): MutableSharedFlow<UniqueEvent?>

    suspend fun checkPacks()

    suspend fun setActiveQuiz(quizPack: QuizPack): DataResponse<QuizPack>
    suspend fun deleteQuiz(quizPack: QuizPack): DataResponse<Unit>
    suspend fun editQuiz(quizPack: QuizPack): DataResponse<QuizPack>

    suspend fun createQuiz(
        numberOfQuestion: Int,
        prioritizeDifficultQuestions: Boolean
    ): DataResponse<List<QuizQuestion>>
}