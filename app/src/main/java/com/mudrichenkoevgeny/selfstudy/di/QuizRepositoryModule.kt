package com.mudrichenkoevgeny.selfstudy.di

import com.mudrichenkoevgeny.selfstudy.data.database.AppDatabase
import com.mudrichenkoevgeny.selfstudy.data.database.type_converter.QuestionBackupTypeConverter
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizRepositoryModule {

    @Provides
    @Singleton
    fun provideQuestionPackRepository(
        database: AppDatabase
    ): QuizRepository {
        return QuizRepositoryImpl(
            database.questionDao(),
            database.quizQuestionDao(),
            QuestionBackupTypeConverter()
        )
    }

}