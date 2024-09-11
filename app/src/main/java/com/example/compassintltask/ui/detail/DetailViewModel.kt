package com.example.compassintltask.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compassintltask.data.model.UIState
import com.example.compassintltask.data.model.UsersUIModel
import com.example.compassintltask.data.remote.DataState
import com.example.compassintltask.data.remote.model.Avatar
import com.example.compassintltask.data.usecase.GetUserInfoUseCase
import com.example.compassintltask.data.usecase.UpdateProfileImage
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
class DetailViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateProfileImage: UpdateProfileImage,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userInfo = MutableLiveData<UsersUIModel>()
    val userInfoLiveData: LiveData<UsersUIModel> = _userInfo

    private val _uiState = MutableStateFlow<UIState>(UIState.InitialState)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun getUserInfo(userId: String) {
        _uiState.value = UIState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoUseCase.invoke(userId = userId).collect { dataState ->
                withContext(Dispatchers.Main) {
                    when (dataState) {
                        is DataState.Success -> {
                            _uiState.value = UIState.ContentState
                            dataState.data?.let {
                                it.password = sessionManager.getPassword().toString()
                                _userInfo.value = it
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


    fun updateUserProfileImage(userId: String, url: String) {
        _uiState.value = UIState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            updateProfileImage.invoke(userId = userId, avatarData = Avatar(url))
                .collect { dataState ->
                    withContext(Dispatchers.Main) {
                        when (dataState) {
                            is DataState.Success -> {
                                _uiState.value = UIState.ContentState
                                dataState.data?.let {
                                    it.password = sessionManager.getPassword().toString()
                                    _userInfo.value = it
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

    fun getCurrentSessionUserId() = sessionManager.getUserId() ?: ""


}
