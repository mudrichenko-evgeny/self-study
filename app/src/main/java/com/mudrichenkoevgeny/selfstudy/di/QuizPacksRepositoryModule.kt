package com.mudrichenkoevgeny.selfstudy.di

import com.mudrichenkoevgeny.selfstudy.data.database.AppDatabase
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizPacksRepositoryModule {

    @Provides
    @Singleton
    fun provideQuizPacksRepository(
        database: AppDatabase
    ): QuizPacksRepository {
        return QuizPacksRepositoryImpl(
            database.quizPacksDao()
        )
    }

}