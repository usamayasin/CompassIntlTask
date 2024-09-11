package com.example.compassintltask.data.remote.service

import com.example.compassintltask.utils.Constants.Network.PARAM_USER_ID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import com.example.compassintltask.data.remote.model.UserResponse
import com.example.compassintltask.data.remote.model.Avatar


interface ApiService {

    @GET("users/{userid}")
    suspend fun getUserInfo(@Path(PARAM_USER_ID) userId: String): Response<UserResponse>

    @POST("sessions/new")
    suspend fun login(@Body requestBody: LoginRequestBody): Response<LoginResponse>

    @POST("users/{userid}/avatar")
    suspend fun updateUserProfileImage(
        @Path(PARAM_USER_ID) userId: String,
        @Body avatarData: Avatar
    ): Response<UserResponse>
}
