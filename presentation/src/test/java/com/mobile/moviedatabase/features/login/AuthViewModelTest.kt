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
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var authViewModel: AuthViewModel

    @Mock
    lateinit var observer: Observer<AuthViewModel.State>

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    lateinit var lifecycle: Lifecycle

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        authViewModel = createViewModel()
        authViewModel.liveData.observeForever(observer)
    }

    @Test
    fun notNull() {
        assertNotNull(authViewModel.liveData)
        assertTrue(authViewModel.liveData.hasObservers())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun isUserExist() {
    }

    @Test
    fun login() {
        runBlocking {
            authViewModel.login(username = "qwerty@gmail.com", password = "123456")
            verify(observer).onChanged(AuthViewModel.State.ShowLoading)
            verify(observer).onChanged(AuthViewModel.State.HideLoading)
            verify(observer).onChanged(AuthViewModel.State.Login)
        }
    }

    private fun createViewModel(): AuthViewModel {
        val authInteractor = mock(AuthInteractor::class.java)
        val userExistInteractor = mock(UserExistInteractor::class.java)
        val requestTokenInteractor = mock(RequestTokenInteractor::class.java)
        val createSessionInteractor = mock(CreateSessionInteractor::class.java)
        return AuthViewModel(
            authInteractor = authInteractor,
            userExistInteractor = userExistInteractor,
            requestTokenInteractor = requestTokenInteractor,
            createSessionInteractor = createSessionInteractor
        )
    }
}