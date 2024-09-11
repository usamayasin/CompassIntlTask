package com.example.compassintltask.data.usecase

import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import com.example.compassintltask.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HandleLoginUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun invoke(
        requestBody: LoginRequestBody
    ): Flow<DataState<LoginResponse>> = flow {

        repository.login(requestBody = requestBody).collect { response ->
            when (response) {
                is DataState.Success -> {
                    response.data?.let { it ->
                        emit(DataState.Success(it))
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