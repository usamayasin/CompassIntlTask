package com.example.compassintltask.data.remote.service

import com.example.compassintltask.data.local.entity.UsersEntity
import com.example.compassintltask.data.local.repository.LocalRepository
import com.example.compassintltask.data.remote.DataState.*
import com.example.compassintltask.data.remote.model.Avatar
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import com.example.compassintltask.data.remote.model.UserResponse
import com.example.compassintltask.utils.SessionManager
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val sessionManager: SessionManager
) : ApiService {

    override suspend fun login(requestBody: LoginRequestBody): Response<LoginResponse> {
        return if (requestBody.username != "Mockuser123" || requestBody.password != "0000") {
            Response.error(
                401,
                CustomMessages.Unauthorized().message.toResponseBody("application/json".toMediaTypeOrNull())
            )
        } else {
            localRepository.insertUserInfo(
                UsersEntity(
                    userId = requestBody.username,
                    username = requestBody.username
                )
            )
            // Added just to demonstrate real API Call
            delay(1000)
            sessionManager.savePassword(requestBody.password)
            Response.success(LoginResponse("Mockuser123", "mockToken"))
        }
    }

    override suspend fun getUserInfo(userId: String): Response<UserResponse> {
        val result = localRepository.getUserInfo(userId)
        return Response.success(UserResponse(result.username, result.profileImagePath))
    }

    override suspend fun updateUserProfileImage(
        userId: String,
        avatarData: Avatar
    ): Response<UserResponse> {
        localRepository.updateUserProfileImagePath(username = userId, avatarUrl = avatarData.avatar)
        val result = localRepository.getUserInfo(userId = userId)
        return Response.success(UserResponse(result.username, result.profileImagePath))
    }
}