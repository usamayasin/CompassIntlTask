package com.example.compassintltask.di.modules

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.remote.model.UserResponse
import com.example.compassintltask.data.remote.service.ApiService
import com.example.compassintltask.data.repository.Repository
import com.example.compassintltask.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The Dagger Module for providing repository instances.
 */

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides

    fun provideRepository(
        userResponseMapper: Mapper<UserResponse, UsersDTO>,
        apiService: ApiService
    ): Repository {
        return RepositoryImpl(apiService, userResponseMapper)
    }
}
