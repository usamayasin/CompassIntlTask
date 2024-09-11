package com.example.compassintltask.data.repository


import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.remote.service.ApiService
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.Avatar
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import com.example.compassintltask.data.remote.model.UserResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This is an implementation of [Repository] to handle communication with [ApiService] server.
 */

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userResponseMapper: Mapper<UserResponse, UsersDTO>,
) : Repository {

    override suspend fun login(requestBody: LoginRequestBody) = flow {
        val apiResponse = apiService.login(requestBody)
        emit(
            if (apiResponse.isSuccessful) {
                DataState.Success(apiResponse.body())
            } else {
                val errorBody = apiResponse.errorBody()?.string()
                DataState.Error(DataState.CustomMessages.Unauthorized(errorBody.toString()))
            }
        )
    }.catch {
        this.emit(
            DataState.Error<LoginResponse>(
                DataState.CustomMessages.SomethingWentWrong(
                    it.message ?: DataState.CustomMessages.BadRequest.toString()
                )
            )
        )
    }

    override suspend fun getUserInfo(userId: String) = flow {
        val apiResponse = apiService.getUserInfo(userId)
        if (apiResponse.isSuccessful) {
            emit(DataState.Success(userResponseMapper.map(apiResponse.body()!!)))
        } else {
            emit(DataState.Error(DataState.CustomMessages.NotFound))
        }
    }.catch {
        emit(
            DataState.Error<UsersDTO>(
                DataState.CustomMessages.SomethingWentWrong(
                    it.message ?: DataState.CustomMessages.BadRequest.toString()
                )
            )
        )
    }

    override suspend fun updateUserProfileImage(userId: String, avatarData: Avatar) = flow {
        val apiResponse =
            apiService.updateUserProfileImage(userId = userId, avatarData = avatarData)
        if (apiResponse.isSuccessful) {
            emit(DataState.Success(userResponseMapper.map(apiResponse.body()!!)))
        } else {
            emit(DataState.Error(DataState.CustomMessages.BadRequest))
        }
    }.catch {
        emit(
            DataState.Error<UsersDTO>(
                DataState.CustomMessages.SomethingWentWrong(
                    it.message ?: DataState.CustomMessages.BadRequest.toString()
                )
            )
        )
    }
}