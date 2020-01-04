package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository

class AuthInteractor(private val userRepository: UserRepository) {

    suspend fun login(username: String, password:String) = userRepository.login(username, password)
}