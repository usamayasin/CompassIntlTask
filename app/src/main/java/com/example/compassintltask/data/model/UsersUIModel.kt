package com.example.compassintltask.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersUIModel(
    var profileImagePath: String? = null,
    var username: String? = null,
    var password:String = ""
) : Parcelable
