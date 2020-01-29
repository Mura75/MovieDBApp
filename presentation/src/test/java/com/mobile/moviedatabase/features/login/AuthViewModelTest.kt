package com.mobile.moviedatabase.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.interactor.AuthInteractor
import com.mobile.domain.interactor.CreateSessionInteractor
import com.mobile.domain.interactor.RequestTokenInteractor
import com.mobile.domain.interactor.UserExistInteractor
import com.mobile.moviedatabase.CoroutinesTestRule
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var observer: Observer<AuthViewModel.State>

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var authInteractor: AuthInteractor

    @Mock
    lateinit var userExistInteractor: UserExistInteractor

    @Mock
    lateinit var requestTokenInteractor: RequestTokenInteractor

    @Mock
    lateinit var createSessionInteractor: CreateSessionInteractor

    lateinit var lifecycle: Lifecycle

    lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        authViewModel = AuthViewModel(
            authInteractor = authInteractor,
            userExistInteractor = userExistInteractor,
            requestTokenInteractor = requestTokenInteractor,
            createSessionInteractor = createSessionInteractor
        )
        authViewModel.liveData.observeForever(observer)
    }

    @Test
    fun `check is liveData exist`() {
        assertNotNull(authViewModel.liveData)
        assertTrue(authViewModel.liveData.hasObservers())
    }


    @Test
    fun `user exist`() {
        `when`(userExistInteractor.isUserExist())
            .thenReturn(true)
        assertEquals(userExistInteractor.isUserExist(), true)
        assertEquals(authViewModel.isUserExist(), true)
    }

    @Test
    fun `user doesn't exist`() {
        `when`(userExistInteractor.isUserExist())
            .thenReturn(false)
        assertEquals(userExistInteractor.isUserExist(), false)
        assertEquals(authViewModel.isUserExist(), false)
    }

    @Test
    fun `login success`() {
        runBlocking {
            `when`(
                requestTokenInteractor.createRequestToken()
            ).thenReturn("")
            `when`(
                authInteractor.login(
                    requestToken = "",
                    username = "qwerty@gmail.com",
                    password = "123456"
                )
            ).thenReturn(true)
            authViewModel.login(username = "qwerty@gmail.com", password = "123456")
            verify(observer).onChanged(AuthViewModel.State.ShowLoading)
            verify(authInteractor).login(
                requestToken = "",
                username = "qwerty@gmail.com",
                password = "123456"
            )
            verify(observer).onChanged(AuthViewModel.State.Login)
            verify(observer).onChanged(AuthViewModel.State.HideLoading)
        }
    }

    @Test
    fun `login failure`() {
        runBlocking {
            `when`(
                requestTokenInteractor.createRequestToken()
            ).thenReturn("")
            `when`(
                authInteractor.login(
                    requestToken = "",
                    username = "qwerty@gmail.com",
                    password = "12345"
                )
            ).thenReturn(false)
            authViewModel.login(username = "qwerty@gmail.com", password = "12345")
            verify(observer).onChanged(AuthViewModel.State.ShowLoading)
            verify(authInteractor).login(
                requestToken = "",
                username = "qwerty@gmail.com",
                password = "12345"
            )
            verify(observer).onChanged(AuthViewModel.State.Error("incorrect login or password"))
            verify(observer).onChanged(AuthViewModel.State.HideLoading)
        }
    }

    @After
    fun tearDown() {

    }
}