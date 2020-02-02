package com.mobile.domain.repository

class UserRepositoryImplTest: UserRepository {

    override suspend fun login(requestToken: String, username: String, password: String): Boolean {
        return true
    }


    override fun isUserExist(): Boolean {
        return true
    }

    override suspend fun createRequestToken(): String {
        return "request_token"
    }

    override suspend fun createSession(requestToken: String) {

    }
}