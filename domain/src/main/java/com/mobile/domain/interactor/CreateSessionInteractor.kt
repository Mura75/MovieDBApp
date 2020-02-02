package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class CreateSessionInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun createSession(requestToken: String) = userRepository.createSession(requestToken)
}