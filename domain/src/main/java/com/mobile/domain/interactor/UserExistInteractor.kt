package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepository

class UserExistInteractor(private val userRepository: UserRepository) {

    fun isUserExist() = userRepository.isUserExist()
}