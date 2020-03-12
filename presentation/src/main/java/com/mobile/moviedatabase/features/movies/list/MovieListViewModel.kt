package com.mobile.moviedatabase.features.movies.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mobile.domain.Movie
import com.mobile.domain.interactor.GetMoviesInteractor
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.exceptions.NoConnectionException
import com.mobile.moviedatabase.core.extensions.launchSafe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieListViewModel @Inject constructor(
    private val getMoviesInteractor: GetMoviesInteractor
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    init {
        loadMovies()
    }

    fun loadMovies(page: Int = 1) {
        addDisposable(
            getMoviesInteractor.getMovies(page)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    State.Result(totalPage = result.first, list = result.second)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (page == 1) {
                        liveData.value = State.ShowLoading
                    }
                }
                .doFinally { liveData.value = State.HideLoading }
                .subscribe(
                    { result -> liveData.value = result },
                    {}
                )
        )
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result( val totalPage: Int, val list: List<Movie>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}