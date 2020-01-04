package com.mobile.moviedatabase.features.login

import androidx.lifecycle.MutableLiveData
import com.mobile.domain.repository.UserRepository
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun isUserExist(): Boolean = userRepository.isUserExist()

    fun login(username: String, password: String) {
        liveData.value = State.ShowLoading
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                userRepository.login(username, password)
            }
            if (result) {
                liveData.value = State.Login
            }
            liveData.value = State.ShowLoading
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