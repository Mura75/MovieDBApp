package com.mobile.domain.interactor

import com.mobile.domain.repository.MovieRepository
import javax.inject.Inject

class MovieDetailInteractor @Inject constructor(private val movieRepository: MovieRepository) {

    fun getMovie(id: Int) = movieRepository.getMovie(id)
}