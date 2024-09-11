package com.example.compassintltask.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.compassintltask.utils.Constants

@Entity(tableName = Constants.USERS_TABLE)
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    var userId: String,
    var profileImagePath: String = "",
    var username:String
)

