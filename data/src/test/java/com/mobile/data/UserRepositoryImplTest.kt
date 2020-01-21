package com.mobile.data

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.repository.UserRepositoryImpl
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.data.storage.LocalPrefStorageImpl
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.`when`
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

    private val deferred = CompletableDeferred(Response.success(JsonObject()))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepositoryImpl(movieApi, localPrefStorage)
    }

    @Test
    fun `user login`() {
        runBlocking {
            `when`(movieApi.login(
                body = JsonObject().apply {
                    addProperty("username", "qwerty@gmail.com")
                    addProperty("password", "123456")
                    addProperty("request_token", "request_token")
                })
            ).thenReturn(deferred)

            val login = userRepository.login(
                requestToken = "request_token",
                username = "qwerty@gmail.com",
                password = "123456"
            )
            assertEquals(login, false)
        }
    }

    @Test
    fun `create user request token`() {
        runBlocking {
            `when`(movieApi.createRequestToken())
                .thenReturn(deferred)
            val token = userRepository.createRequestToken()
            assertEquals(token, "")
        }
    }

    @Test
    fun `create user session id`() {
        runBlocking {
            val body = JsonObject().apply {
                addProperty("request_token", "token")
            }
            `when`(movieApi.createSession(body))
                .thenReturn(deferred)
        }
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
}