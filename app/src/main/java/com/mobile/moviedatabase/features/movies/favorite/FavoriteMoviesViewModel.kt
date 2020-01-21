package com.mobile.moviedatabase.features.movies.favorite

import com.mobile.moviedatabase.core.base.BaseViewModel
import com.mobile.moviedatabase.data.repository.MovieRepository

class FavoriteMoviesViewModel(
    private val moviesRepository: MovieRepository
) : BaseViewModel() {

    override fun handleError(e: Throwable) {

    }

}