package com.mobile.moviedatabase.features.movies.detail

import androidx.lifecycle.MutableLiveData
import com.mobile.domain.Movie
import com.mobile.domain.interactor.MovieDetailInteractor
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.exceptions.NoConnectionException
import com.mobile.moviedatabase.core.extensions.launchSafe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val movieDetailInteractor: MovieDetailInteractor
): BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun getMovieDetail(movieId: Int) {
        addDisposable(
            movieDetailInteractor.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .map { movie -> State.Result(movie) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { liveData.value = State.ShowLoading }
                .doFinally { liveData.value = State.HideLoading }
                .subscribe(
                    { result -> liveData.value = result },
                    { error -> liveData.value = State.Error(error.localizedMessage) }
                )
        )
    }

    sealed class State() {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val movie: Movie): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}