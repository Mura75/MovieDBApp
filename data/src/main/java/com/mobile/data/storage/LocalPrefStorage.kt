package com.mobile.data.storage

interface LocalPrefStorage {

    fun saveData(key: String, value: Any)

    fun getString(key: String): String?

    fun getInt(key: String): Int

    fun exist(key: String): Boolean
}