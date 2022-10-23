package com.mudrichenko.evgeniy.selfstudy.di

import android.content.Context
import com.mudrichenko.evgeniy.selfstudy.domain.file_repository.FileRepository
import com.mudrichenko.evgeniy.selfstudy.domain.file_repository.FileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileRepositoryModule {

    @Provides
    @Singleton
    fun provideFileRepository(
        @ApplicationContext context: Context
    ): FileRepository {
        return FileRepositoryImpl(
            context
        )
    }

}