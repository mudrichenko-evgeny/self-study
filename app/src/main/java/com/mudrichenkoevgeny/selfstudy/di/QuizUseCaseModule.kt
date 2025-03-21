package com.mudrichenkoevgeny.selfstudy.di

import com.mudrichenkoevgeny.selfstudy.domain.file_repository.FileRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_use_case.QuizUseCase
import com.mudrichenkoevgeny.selfstudy.domain.quiz_use_case.QuizUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizUseCaseModule {

    @Provides
    @Singleton
    fun provideQuizUseCase(
        fileRepository: FileRepository,
        quizRepository: QuizRepository,
        quizPacksRepository: QuizPacksRepository
    ): QuizUseCase {
        return QuizUseCaseImpl(
            fileRepository,
            quizRepository,
            quizPacksRepository
        )
    }

}