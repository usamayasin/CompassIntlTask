package com.example.compassintltask.data.usecase

import com.example.compassintltask.base.mapper.Mapper
import com.example.compassintltask.data.model.UsersDTO
import com.example.compassintltask.data.model.UsersUIModel
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: Repository,
    private val userDTOMapper: Mapper<UsersDTO, UsersUIModel>
) {

    suspend fun invoke(
        userId: String
    ): Flow<DataState<UsersUIModel>> = flow {

        repository.getUserInfo(userId = userId).collect { response ->
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