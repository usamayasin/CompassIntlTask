package com.example.compassintltask.data.mapper

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.remote.model.UserResponse

class UserResponseMapper : Mapper<UserResponse, UsersDTO> {
    override fun map(from: UserResponse) =
        UsersDTO(
            userId = from.username,
            username = from.username,
            profileImagePath = from.avatarUrl
        )
}
