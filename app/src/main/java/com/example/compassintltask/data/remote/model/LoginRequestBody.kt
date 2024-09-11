package com.example.compassintltask.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequestBody(val username: String, val password: String) : Parcelable

