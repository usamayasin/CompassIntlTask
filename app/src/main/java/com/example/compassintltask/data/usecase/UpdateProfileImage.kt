package com.example.compassintltask.data.usecase

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.model.UsersUIModel
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.Avatar
import com.example.compassintltask.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProfileImage @Inject constructor(
    private val repository: Repository,
    private val userDTOMapper: Mapper<UsersDTO, UsersUIModel>
) {
    suspend fun invoke(
        userId: String, avatarData: Avatar
    ): Flow<DataState<UsersUIModel>> = flow {

        repository.updateUserProfileImage(userId = userId, avatarData = avatarData)
            .collect { response ->
                when (response) {
                    is DataState.Success -> {
                        response.data?.let { it ->
                            emit(DataState.Success(userDTOMapper.map(it)))
                        }
                    }

                    is DataState.Error -> {
                        emit(DataState.Error(response.error))
                    }

                    else -> {
                        emit(DataState.Error(response.error))
                    }
                }

            }
    }

}