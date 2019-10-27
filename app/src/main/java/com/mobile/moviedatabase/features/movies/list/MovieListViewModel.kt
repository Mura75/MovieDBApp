package com.mobile.moviedatabase.features.movies.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mobile.moviedatabase.core.exceptions.NoConnectionException
import com.mobile.moviedatabase.core.extensions.launchSafe
import com.mobile.moviedatabase.features.movies.data.Movie
import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.features.movies.data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    init {
        loadMovies()
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

    fun loadMovies(page: Int = 1) {
        uiScope.launchSafe(::handleError) {
            if (page == 1) {
                liveData.value =
                    State.ShowLoading
            }
            val result = withContext(Dispatchers.IO) {
                val response = movieRepository.getMovies(page)
                val list = response?.results ?: emptyList()
                val totalPage = response?.totalPages ?: 0
                Pair(totalPage, list)
            }
            Log.d("result_movies", result.second.size.toString())
            Log.d("result_movies_pages", result.first.toString())
            liveData.postValue(
                State.Result(
                    totalPage = result.first,
                    list = result.second
                )
            )
            liveData.value =
                State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result( val totalPage: Int, val list: List<Movie>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}