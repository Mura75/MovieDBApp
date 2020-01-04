package com.mobile.moviedatabase.features.movies.detail

import androidx.lifecycle.MutableLiveData
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.core.exceptions.NoConnectionException
import com.mobile.moviedatabase.core.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun getMovieDetail(movieId: Int) {
        uiScope.launchSafe(::handleError) {
            liveData.value = State.ShowLoading
            val movie = withContext(Dispatchers.IO) {
                movieRepository.getMovie(movieId)
            }
            movie?.let { liveData.postValue(State.Result(it)) }
            liveData.value = State.HideLoading
        }
    }

    override fun handleError(e: Throwable) {
        liveData.value =
            State.HideLoading
        if (e is NoConnectionException) {
            liveData.value =
                State.IntError(e.messageInt)
        } else {
            liveData.value =
                State.Error(e.localizedMessage)
        }
    }

    sealed class State() {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val movie: Movie): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}