package com.example.compassintltask.data.remote

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */

sealed class DataState<T>(
    val data: T? = null,
    val error: CustomMessages = CustomMessages.SomethingWentWrong("Something Went Wrong")
) {
    class Success<T>(data: T?) : DataState<T>(data)
    class Error<T>(customMessages: CustomMessages) : DataState<T>(error = customMessages)
    sealed class CustomMessages(open val message: String = "") {
        object Timeout : CustomMessages()
        object EmptyData : CustomMessages()
        object ServerBusy : CustomMessages()
        object HttpException : CustomMessages()
        object SocketTimeOutException : CustomMessages()
        object NoInternet : CustomMessages()
        data class Unauthorized(override var message:String = "Invalid login. Please check your credentials.") : CustomMessages()
        object InternalServerError : CustomMessages()
        object BadRequest : CustomMessages()
        object Conflict : CustomMessages()
        object NotFound : CustomMessages("User Not Found")
        object NotAcceptable : CustomMessages()
        object ServiceUnavailable : CustomMessages()
        object Forbidden : CustomMessages()
        data class SomethingWentWrong(val error: String) : CustomMessages(message = error)
    }
}
