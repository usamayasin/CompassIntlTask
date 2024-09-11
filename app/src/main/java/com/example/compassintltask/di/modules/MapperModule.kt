package com.example.compassintltask.di.modules

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.local.entity.UsersEntity
import com.example.compassintltask.data.local.mapper.UserEntityMapper
import com.example.compassintltask.data.mapper.UserDTOMapper
import com.example.compassintltask.data.mapper.UserResponseMapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.model.UsersUIModel
import com.example.compassintltask.data.remote.model.UserResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The DI Module for providing mappers.
 */

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Singleton
    @Provides
    fun provideUserEntityMapper(): Mapper<UsersEntity, UsersDTO> {
        return UserEntityMapper()
    }

    @Singleton
    @Provides
    fun provideUserDTOMapper(): Mapper<UsersDTO, UsersUIModel> {
        return UserDTOMapper()
    }

    @Singleton
    @Provides
    fun provideUserResponseMapper(): Mapper<UserResponse, UsersDTO> {
        return UserResponseMapper()
    }


}