package com.mobile.moviedatabase.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.interactor.*
import com.mobile.moviedatabase.CoroutinesTestRule
import com.mobile.moviedatabase.RxRule
import io.reactivex.Single
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxRule()

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

    @Mock
    lateinit var saveTokenInteractor: SaveTokenInteractor

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
            createSessionInteractor = createSessionInteractor,
            saveTokenInteractor = saveTokenInteractor
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
        `when`(requestTokenInteractor.createRequestToken())
            .thenReturn(Single.just("request_token_123"))

        `when`(
            authInteractor.login(
                requestToken = "request_token_123",
                username = "qwerty@gmail.com",
                password = "123456"
            )
        ).thenReturn(Single.just(Pair("request_token_123", true)))

        authViewModel.login(
            username = "qwerty@gmail.com",
            password = "123456"
        )
        verify(observer).onChanged(AuthViewModel.State.ShowLoading)
        verify(observer).onChanged(AuthViewModel.State.HideLoading)
        verify(observer).onChanged(AuthViewModel.State.Login)
    }

    @Test
    fun `login error`() {
        `when`(requestTokenInteractor.createRequestToken())
            .thenReturn(Single.just("request_token_123"))

        `when`(
            authInteractor.login(
                requestToken = "request_token_123",
                username = "qwerty@gmail.com",
                password = "12345"
            )
        ).thenReturn(Single.error(Throwable("invalid username or password")))

        authViewModel.login(
            username = "qwerty@gmail.com",
            password = "12345"
        )
        verify(observer).onChanged(AuthViewModel.State.ShowLoading)
        verify(observer).onChanged(AuthViewModel.State.HideLoading)
        verify(observer).onChanged(AuthViewModel.State.Error("invalid username or password"))
    }

    @After
    fun tearDown() {
        reset(observer)
        reset(lifecycleOwner)
        reset(authInteractor)
        reset(userExistInteractor)
        reset(requestTokenInteractor)
        reset(createSessionInteractor)
        Dispatchers.resetMain()
    }
}