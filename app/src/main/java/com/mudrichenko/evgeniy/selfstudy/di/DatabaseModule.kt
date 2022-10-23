package com.mudrichenko.evgeniy.selfstudy.di

import android.content.Context
import androidx.room.Room
import com.mudrichenko.evgeniy.selfstudy.data.database.AppDatabase
import com.mudrichenko.evgeniy.selfstudy.data.database.DatabaseMigration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .addMigrations(*DatabaseMigration().migrations)
            .build()
    }

}