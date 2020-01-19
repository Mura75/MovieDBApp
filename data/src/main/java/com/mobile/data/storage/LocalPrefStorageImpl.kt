package com.mobile.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class LocalPrefStorageImpl @Inject constructor(context: Context): LocalPrefStorage {

    companion object {
        const val SESSION_ID = "session_id"
        const val REQUEST_TOKEN = "request_token"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    
    override fun saveData(key: String, value: Any) {
        when(value) {
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
            is Long -> sharedPreferences.edit().putLong(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
        }
    }

    override fun getString(key: String): String = sharedPreferences.getString(key, "") ?: ""

    override fun getInt(key: String) = sharedPreferences.getInt(key, 0)

    override fun exist(key: String) = sharedPreferences.contains(key)
}