package com.example.compassintltask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.compassintltask.data.local.entity.UsersEntity
import com.example.compassintltask.utils.Constants.USERS_TABLE

@Dao
abstract class UserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertUserInfo(usersEntity: UsersEntity): Long

    @Query("UPDATE $USERS_TABLE SET profileImagePath = :avatarUrl WHERE username = :username")
    abstract suspend fun updateUserProfileImagePath(username: String, avatarUrl: String)

    @Query("SELECT * FROM $USERS_TABLE WHERE userId = :userId LIMIT 1")
    abstract fun getUserInfo(userId: String): UsersEntity

}