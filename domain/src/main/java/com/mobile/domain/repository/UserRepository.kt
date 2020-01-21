package com.mobile.domain.repository

interface UserRepository {
    suspend fun login(username: String, password: String): Boolean
    fun isUserExist(): Boolean
}