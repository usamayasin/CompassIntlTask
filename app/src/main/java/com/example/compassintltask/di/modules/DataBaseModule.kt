package com.example.compassintltask.di.modules

import android.content.Context
import com.example.compassintltask.data.local.AppDatabase
import com.example.compassintltask.data.local.dao.UserDao
import com.example.compassintltask.data.local.repository.LocalRepository
import com.example.compassintltask.data.local.repository.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * The DI Module for providing database components.
 */

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun providesLocalRepository(userDao: UserDao): LocalRepository {
        return LocalRepositoryImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideUsersDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.usersDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}