package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun login(requestToken: String, username: String?, password:String?): Boolean {
        if (username.isNullOrEmpty()) {
            throw Exception("empty username")
        }
        if (password.isNullOrEmpty()) {
            throw Exception("empty password")
        }
        return userRepository.login(
            requestToken = requestToken,
            username = username,
            password = password
        )
    }

}