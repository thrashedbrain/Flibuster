package com.thrashed.flibuster.data.database.di

import android.content.Context
import androidx.room.Room
import com.thrashed.flibuster.data.database.dao.BookDao
import com.thrashed.flibuster.data.database.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "db")
            .build()

    @Provides
    fun provideBookDao(appDatabase: AppDatabase): BookDao = appDatabase.booksDao()
}