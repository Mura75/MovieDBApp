package com.mobile.moviedatabase.features.movies.favorite

import com.mobile.domain.repository.MovieRepository
import com.mobile.moviedatabase.core.base.BaseViewModel

class FavoriteMoviesViewModel(
    private val moviesRepository: MovieRepository
) : BaseViewModel() {

    override fun handleError(e: Throwable) {

    }

}