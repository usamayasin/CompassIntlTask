package com.example.compassintltask.data.local.mapper

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.local.entity.UsersEntity
import com.example.compassintltask.data.model.UsersDTO

class UserEntityMapper : Mapper<UsersEntity, UsersDTO> {
    override fun map(from: UsersEntity) =
        UsersDTO(
            userId = from.userId,
            profileImagePath = from.profileImagePath?: "",
            username = from.username
        )
}
