package com.example.compassintltask.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(val userid: String, val token: String) : Parcelable
