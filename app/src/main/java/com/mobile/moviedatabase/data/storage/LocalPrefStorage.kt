package com.mobile.moviedatabase.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class LocalPrefStorage constructor(context: Context) {

    companion object {
        const val SESSION_ID = "session_id"
        const val REQUEST_TOKEN = "request_token"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    
    fun saveData(key: String, value: Any) {
        when(value) {
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
            is Long -> sharedPreferences.edit().putLong(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
        }
    }

    fun getString(key: String) = sharedPreferences.getString(key, "")

    fun getInt(key: String) = sharedPreferences.getInt(key, 0)

    fun exist(key: String) = sharedPreferences.contains(key)
}