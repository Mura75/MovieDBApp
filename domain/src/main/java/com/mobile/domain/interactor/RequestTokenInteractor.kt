package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class RequestTokenInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun createRequestToken() = userRepository.createRequestToken()
}