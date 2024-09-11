package com.example.compassintltask.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compassintltask.data.model.UIState
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.LoginRequestBody
import com.example.compassintltask.data.remote.model.LoginResponse
import com.example.compassintltask.data.usecase.HandleLoginUseCase
import com.example.compassintltask.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val handleLoginUseCase: HandleLoginUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginRequest = MutableLiveData<LoginResponse>()
    val loginLiveData: LiveData<LoginResponse> = _loginRequest

    private val _uiState = MutableStateFlow<UIState>(UIState.InitialState)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun handleLogin(requestBody: LoginRequestBody) {
        _uiState.value = UIState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            handleLoginUseCase.invoke(requestBody).collect { dataState ->
                withContext(Dispatchers.Main) {
                    when (dataState) {
                        is DataState.Success -> {
                            _uiState.value = UIState.ContentState
                            dataState.data?.let {
                                _loginRequest.value = it
                                // saving session information
                                sessionManager.saveSession(it.token, it.userid)
                            }
                        }

                        is DataState.Error -> {
                            _uiState.value = UIState.ErrorState(dataState.error.message)
                        }
                    }
                }
            }
        }
    }

    fun checkIfSessionExist() = sessionManager.hasActiveSession()
    fun getCurrentSessionUserId() = sessionManager.getUserId() ?: ""


}
