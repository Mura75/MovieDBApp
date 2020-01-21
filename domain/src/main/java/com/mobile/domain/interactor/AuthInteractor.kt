package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun login(username: String, password:String) = userRepository.login(username, password)
}