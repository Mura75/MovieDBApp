package com.mobile.moviedatabase.features.login

import android.util.Log
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
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.verification.VerificationMode

@ExperimentalCoroutinesApi
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

    private val authViewModel: AuthViewModel by lazy {
        AuthViewModel(
            authInteractor = authInteractor,
            userExistInteractor = userExistInteractor,
            requestTokenInteractor = requestTokenInteractor,
            createSessionInteractor = createSessionInteractor
        )
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        authViewModel.liveData.observeForever(observer)
    }

    @Test
    fun notNull() {
        assertNotNull(authViewModel.liveData)
        assertTrue(authViewModel.liveData.hasObservers())
    }


    @Test
    fun `check is user exist`() {
        `when`(userExistInteractor.isUserExist())
            .thenReturn(true)
        assertEquals(userExistInteractor.isUserExist(), true)
    }

    @Test
    fun `login success`() {
        runBlocking {
            `when`(
                requestTokenInteractor.createRequestToken()
            ).thenReturn("")
            `when`(
                authInteractor.login(requestToken = "", username = "qwerty@gmail.com", password = "123456")
            ).thenReturn(true)
            authViewModel.login(username = "qwerty@gmail.com", password = "123456")
            verify(observer).onChanged(AuthViewModel.State.ShowLoading)
            verify(authInteractor).login(requestToken = "", username = "qwerty@gmail.com", password = "123456")
            verify(observer).onChanged(AuthViewModel.State.HideLoading)
            verify(observer).onChanged(AuthViewModel.State.Login)
        }
    }

    @After
    fun tearDown() {

    }

}