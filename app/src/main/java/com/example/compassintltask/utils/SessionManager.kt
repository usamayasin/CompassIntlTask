package com.example.compassintltask.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import javax.inject.Inject


class SessionManager @Inject constructor(context: Context) {

    private var masterKeyAlias: String = MasterKeys.getOrCreate(AES256_GCM_SPEC)
    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PASSWORD = "password"
    }

    fun saveSession(token: String, userId: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_SESSION_TOKEN, token)
            putString(KEY_USER_ID, userId)
            apply()
        }
    }

    fun savePassword(password:String){
        with(sharedPreferences.edit()) {
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    fun getSessionToken(): String? {
        return sharedPreferences.getString(KEY_SESSION_TOKEN, null)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clearSession() {
        with(sharedPreferences.edit()) {
            remove(KEY_SESSION_TOKEN)
            remove(KEY_USER_ID)
            apply()
        }
    }

    fun hasActiveSession(): Boolean {
        return getSessionToken() != null
    }
}