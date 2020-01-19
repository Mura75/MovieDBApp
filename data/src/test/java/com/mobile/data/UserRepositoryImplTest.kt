package com.mobile.data

import com.google.gson.JsonObject
import com.mobile.data.network.MovieApi
import com.mobile.data.repository.UserRepositoryImpl
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.data.storage.LocalPrefStorageImpl
import com.mobile.domain.repository.UserRepository
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
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
        MockitoAnnotations.initMocks(this);
        userRepository = UserRepositoryImpl(movieApi, localPrefStorage)
    }

    @Test
    fun login() {
        runBlocking {
            val deferred = CompletableDeferred(Response.success(JsonObject()))
            `when`(movieApi.login(JsonObject()))
                .thenReturn(deferred)

            val login = userRepository.login("qwerty@gmail.com", "123456")
            assertEquals(login, true)
        }
    }

    @Test
    fun createRequestToken() {
        runBlocking {
            val deferred = CompletableDeferred(Response.success(JsonObject()))
            `when`(movieApi.createRequestToken())
                .thenReturn(deferred)

            val token = userRepository.createRequestToken()
            assertEquals(token, "")
        }
    }

    @Test
    fun createSession() {
        runBlocking {
            val deferred = CompletableDeferred(Response.success(JsonObject()))
            `when`(movieApi.createSession(JsonObject()))
                .thenReturn(deferred)

            val sessionToken = userRepository.createSession("")
            assertEquals(sessionToken, "")
        }

    }

    @Test
    fun isUserExist() {

    }
}

fun <T> T.toDeferred() = GlobalScope.async { this@toDeferred }