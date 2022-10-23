package com.mudrichenko.evgeniy.selfstudy.data.model.converter

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuizQuestionEntity

fun QuizQuestion.toEntity(): QuizQuestionEntity {
    this.let { model ->
        return QuizQuestionEntity().apply {
            this.questionId = model.questionId
            this.order = model.order
            this.content = model.content
            this.correctAnswer = model.correctAnswer
            this.userAnswerText = model.userAnswerText
            this.hasAudioRecord = model.hasAudioRecord
            this.answerStatus = model.answerStatus
        }
    }
}

fun QuizQuestionEntity.toModel(): QuizQuestion {
    this.let { entity ->
        return QuizQuestion(
            questionId = entity.questionId,
            order = entity.order,
            content = entity.content,
            correctAnswer = entity.correctAnswer,
            userAnswerText = entity.userAnswerText,
            hasAudioRecord = entity.hasAudioRecord == true,
            answerStatus = entity.answerStatus
        )
    }
}