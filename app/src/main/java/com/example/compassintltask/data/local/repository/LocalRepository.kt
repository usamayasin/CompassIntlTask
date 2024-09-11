package com.example.compassintltask.data.local.repository

import com.example.compassintltask.data.local.entity.UsersEntity

interface LocalRepository {
    suspend fun updateUserProfileImagePath(username: String, avatarUrl: String)
    suspend fun insertUserInfo(userEntity: UsersEntity): Long
    suspend fun getUserInfo(userId: String): UsersEntity
}