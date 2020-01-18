package com.mobile.domain.repository

import com.mobile.domain.repository.UserRepository

class UserRepositoryImplTest : UserRepository {

    override suspend fun login(username: String, password: String): Boolean {
        return true
    }

    override fun isUserExist(): Boolean {
        return true
    }
}