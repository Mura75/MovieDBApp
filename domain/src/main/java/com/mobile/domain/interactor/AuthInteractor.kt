package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun login(username: String?, password:String?) {
        if (username.isNullOrEmpty()) {
            throw Exception("empty username")
        }
        if (password.isNullOrEmpty()) {
            throw Exception("empty password")
        }
        userRepository.login(username, password)
    }

}