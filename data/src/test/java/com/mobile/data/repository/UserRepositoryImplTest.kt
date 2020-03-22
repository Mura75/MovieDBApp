package com.mobile.data.repository

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.storage.LocalPrefStorage
import io.reactivex.Single
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

class UserRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var movieApi: MovieApi

    @Mock
    lateinit var localPrefStorage: LocalPrefStorage

    lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepositoryImpl(movieApi, localPrefStorage)
    }

    @Test
    fun `user login`() {
        val body = JsonObject().apply {
            addProperty("username", "qwerty@gmail.com")
            addProperty("password", "123456")
            addProperty("request_token", "request_token")
        }

        val bodyResult = JsonObject().apply {
            addProperty("request_token", "request_token")
            addProperty("success", true)
        }

        `when`(movieApi.login(body = body))
            .thenReturn(Single.just(Response.success(bodyResult)))

        userRepository.login(
            requestToken = "request_token",
            username = "qwerty@gmail.com",
            password = "123456"
        ).test()
            .assertResult(Pair("request_token", true))
            .assertNoErrors()
    }

    @Test
    fun `create user request token`() {
        val resultBody = JsonObject().apply {
            addProperty("request_token", "request_token_123")
        }

        `when`(movieApi.createRequestToken())
            .thenReturn(Single.just(Response.success(resultBody)))

        userRepository.createRequestToken()
            .test()
            .assertResult("request_token_123")
            .assertNoErrors()
    }

    @Test
    fun `create user session id`() {
        val body = JsonObject().apply {
            addProperty("request_token", "token")
        }

        val resultBody = JsonObject().apply {
            addProperty("session_id", "session_id_123")
        }

        val response = Response.success(resultBody)
        `when`(movieApi.createSession(body))
            .thenReturn(Single.just(response))

        userRepository.createSession("token")
            .test()
            .assertResult("session_id_123")
            .assertNoErrors()
    }

    @Test
    fun `check is user exist`() {
        `when`(localPrefStorage.getString("request_token"))
            .thenReturn("request_token")
        `when`(localPrefStorage.getString("session_id"))
            .thenReturn("session_id")
        val userExist = userRepository.isUserExist()
        assertEquals(userExist, true)
    }

    @After
    fun tearDown() {
        reset(movieApi)
        reset(localPrefStorage)
    }
}