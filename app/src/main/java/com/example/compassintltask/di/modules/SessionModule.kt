package com.example.compassintltask.di.modules

import android.app.Application
import com.example.compassintltask.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The DI Module for providing SessionManager.
 */

@Module
@InstallIn(SingletonComponent::class)
class SessionModule {

    @Singleton
    @Provides
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(application.applicationContext)
    }

}