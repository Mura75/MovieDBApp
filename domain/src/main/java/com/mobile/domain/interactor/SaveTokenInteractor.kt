package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class SaveTokenInteractor @Inject constructor(private val userRepository: UserRepository) {

    fun saveToken(token: String) = userRepository.saveToken(token)
}