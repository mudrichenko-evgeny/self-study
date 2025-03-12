package com.mudrichenkoevgeny.selfstudy.data.model.converter

import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.data.model.entity.QuizPackEntity

@Suppress("unused")
fun QuizPack.toEntity(): QuizPackEntity {
    this.let { model ->
        return QuizPackEntity().apply {
            this.id = model.id
            this.name = model.name
            this.fileName = model.fileName
            this.isUserPack = model.isUserPack
            this.isActive = model.isActive
        }
    }
}

fun QuizPackEntity.toModel(): QuizPack {
    this.let { entity ->
        return QuizPack(
            id = entity.id,
            name = entity.name,
            fileName = entity.fileName,
            isUserPack = entity.isUserPack,
            isActive = entity.isActive
        )
    }
}