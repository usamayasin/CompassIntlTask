package com.example.compassintltask.data.model

sealed class UIState{

    object InitialState : UIState()
    object LoadingState : UIState()
    object ContentState : UIState()
    class ErrorState(val message: String) : UIState()
}

