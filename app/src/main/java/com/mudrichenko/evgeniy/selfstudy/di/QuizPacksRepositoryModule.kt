package com.mudrichenko.evgeniy.selfstudy.di

import com.mudrichenko.evgeniy.selfstudy.data.database.AppDatabase
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository.QuizPacksRepositoryImpl
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