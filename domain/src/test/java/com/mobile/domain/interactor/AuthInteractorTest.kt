package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepositoryImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInteractorTest {

    private val userRepository = UserRepositoryImplTest()

    private lateinit var authInteractor: AuthInteractor

    @Before
    fun initTest() {
        authInteractor = AuthInteractor(userRepository)
    }

    @Test
    fun `test user login pass`() {
        authInteractor.login(
            requestToken = "request_token",
            username = "qwerty@gmail.com",
            password = "1234556"
        ).test()
            .assertResult(Pair("request_token", true))
            .assertNoErrors()
    }
}