package com.example.compassintltask.data.remote

import com.example.compassintltask.MyApplication
import com.example.compassintltask.utils.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

open class ConnectivityInterceptor : Interceptor {

    private val isConnected: Boolean
        get() {
            return isInternetAvailable(MyApplication.getInstance())
        }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!isConnected) {
            throw Throwable(DataState.CustomMessages.NoInternet.message)
        }
        return chain.proceed(originalRequest)
    }
}