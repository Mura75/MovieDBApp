package com.mobile.domain.repository

interface UserRepository {
    suspend fun login(requestToken: String, username: String, password: String): Boolean
    fun isUserExist(): Boolean
    suspend fun createRequestToken(): String
    suspend fun createSession(requestToken: String)
}