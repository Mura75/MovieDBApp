package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class UserExistInteractor @Inject constructor(private val userRepository: UserRepository) {

    fun isUserExist() = userRepository.isUserExist()
}