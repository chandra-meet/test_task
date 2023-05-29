package com.example.to_dolist.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.to_dolist.data.local.AppDatabase
import com.example.to_dolist.data.local.ToDoDao
import com.example.to_dolist.data.repository.ToDoRepositoryImpl
import com.example.to_dolist.domain.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "to_do_db")
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    @Singleton
    fun provideToDoDao(appDatabase: AppDatabase): ToDoDao = appDatabase.toDoDao


    @Provides
    @Singleton
    fun provideToDoRepository(toDoDao: ToDoDao): ToDoRepository = ToDoRepositoryImpl(toDoDao)


}