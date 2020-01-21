package com.mobile.moviedatabase.features.login

import androidx.lifecycle.MutableLiveData
import com.mobile.domain.interactor.AuthInteractor
import com.mobile.domain.interactor.UserExistInteractor
import com.mobile.domain.repository.UserRepository
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val userExistInteractor: UserExistInteractor
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun isUserExist(): Boolean = userExistInteractor.isUserExist()

    fun login(username: String, password: String) {
        liveData.value = State.ShowLoading
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                authInteractor.login(username, password)
            }
            liveData.value = State.ShowLoading
            if (result) {
                liveData.value = State.Login
            }
        }
    }

    override fun handleError(e: Throwable) {
        e.printStackTrace()
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        object Login : State()
        data class Error(val error: String?) : State()
        data class IntError(val error: Int) : State()
    }
}