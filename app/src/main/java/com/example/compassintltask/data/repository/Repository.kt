package com.example.compassintltask.data.repository

import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.Avatar
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getUserInfo(userId: String): Flow<DataState<UsersDTO>>
    suspend fun login(requestBody: LoginRequestBody): Flow<DataState<LoginResponse>>
    suspend fun updateUserProfileImage(
        userId: String,
        avatarData: Avatar
    ): Flow<DataState<UsersDTO>>

}