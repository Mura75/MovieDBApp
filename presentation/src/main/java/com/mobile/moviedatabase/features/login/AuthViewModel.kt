package com.mobile.moviedatabase.features.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mobile.domain.interactor.AuthInteractor
import com.mobile.domain.interactor.CreateSessionInteractor
import com.mobile.domain.interactor.RequestTokenInteractor
import com.mobile.domain.interactor.UserExistInteractor
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val userExistInteractor: UserExistInteractor,
    private val requestTokenInteractor: RequestTokenInteractor,
    private val createSessionInteractor: CreateSessionInteractor
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun isUserExist(): Boolean = userExistInteractor.isUserExist()

    fun login(username: String, password: String) {
        liveData.value = State.ShowLoading
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                val requestToken = requestTokenInteractor.createRequestToken()
                createSessionInteractor.createSession(requestToken)
                authInteractor.login(requestToken, username, password)
            }
            if (result) {
                liveData.value = State.Login
            } else {
                liveData.value = State.Error("incorrect login or password")
            }
            liveData.value = State.HideLoading
        }
    }

    override fun handleError(e: Throwable) {
        Log.e("error", e.toString())
        liveData.value = State.HideLoading
        liveData.value = State.Error(e.localizedMessage)
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        object Login : State()
        data class Error(val error: String?) : State()
        data class IntError(val error: Int) : State()
    }
}