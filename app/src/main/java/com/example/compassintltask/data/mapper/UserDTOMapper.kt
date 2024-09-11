package com.example.compassintltask.data.mapper

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.model.UsersUIModel

class UserDTOMapper : Mapper<UsersDTO, UsersUIModel> {
    override fun map(from: UsersDTO) =
        UsersUIModel(username = from.username, profileImagePath = from.profileImagePath)
}
