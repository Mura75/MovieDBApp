package com.mobile.domain.interactor

import com.mobile.domain.repository.UserRepositoryImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserExistInteractorTest {

    private val userRepository =
        UserRepositoryImplTest()

    private lateinit var userExistInteractor: UserExistInteractor

    @Before
    fun initTest() {
        userExistInteractor = UserExistInteractor(userRepository)
    }

    @Test
    fun `test user exist`() {
        runBlocking {
            val isExist = userExistInteractor.isUserExist()
            assertEquals(isExist, true)
        }
    }
}
