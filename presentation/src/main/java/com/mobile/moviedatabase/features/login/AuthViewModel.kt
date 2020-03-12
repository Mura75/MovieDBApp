package com.mobile.moviedatabase.features.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mobile.domain.interactor.*
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.extensions.launchSafe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val userExistInteractor: UserExistInteractor,
    private val requestTokenInteractor: RequestTokenInteractor,
    private val createSessionInteractor: CreateSessionInteractor,
    private val saveTokenInteractor: SaveTokenInteractor
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun isUserExist(): Boolean = userExistInteractor.isUserExist()

    fun login(username: String, password: String) {
        addDisposable(
            requestTokenInteractor.createRequestToken()
                .subscribeOn(Schedulers.io())
                .flatMap { token -> authInteractor.login(token, username, password) }
                .doOnSuccess { pair -> saveTokenInteractor.saveToken(pair.first) }
                .map { pair -> pair.second }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { liveData.value = State.ShowLoading }
                .doFinally { liveData.value = State.HideLoading }
                .subscribe(
                    { result ->
                        Log.d("result_login", result.toString())
                        if (result) {
                            liveData.value = State.Login
                        } else {
                            liveData.value = State.Error("incorrect login or password")
                        }
                    },
                    { error ->
                        Log.d("result_login", error.toString())
                        liveData.value = State.Error(error.localizedMessage)
                    }
                )
        )
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        object Login : State()
        data class Error(val error: String?) : State()
        data class IntError(val error: Int) : State()
    }
}