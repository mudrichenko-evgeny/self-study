package com.mudrichenko.evgeniy.selfstudy.data.model.converter

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.Question
import com.mudrichenko.evgeniy.selfstudy.data.model.entity.QuestionEntity

fun Question.toEntity(): QuestionEntity {
    this.let { model ->
        return QuestionEntity().apply {
            this.id = model.id
            this.content = model.content
            this.answer = model.answer
            this.attemptCount = model.attemptCount
            this.correctCount = model.correctCount
        }
    }
}

fun QuestionEntity.toModel(): Question {
    this.let { entity ->
        return Question(
            id = entity.id,
            content = entity.content,
            answer = entity.answer,
            attemptCount = entity.attemptCount,
            correctCount = entity.correctCount
        )
    }
}