package com.example.compassintltask.data.local.repository

import com.example.compassintltask.data.local.dao.UserDao
import com.example.compassintltask.data.local.entity.UsersEntity
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private var userDao: UserDao) : LocalRepository {

    override suspend fun updateUserProfileImagePath(
        username: String,
        avatarUrl: String
    ) {
        userDao.updateUserProfileImagePath(username = username, avatarUrl = avatarUrl)
    }

    override suspend fun insertUserInfo(userEntity: UsersEntity):Long {
        return userDao.insertUserInfo(usersEntity = userEntity)
    }

    override suspend fun getUserInfo(userId: String): UsersEntity {
        return userDao.getUserInfo(userId = userId)
    }

}