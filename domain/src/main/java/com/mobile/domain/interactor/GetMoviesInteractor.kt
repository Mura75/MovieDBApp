package com.mobile.domain.interactor

import com.mobile.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesInteractor @Inject constructor(private val movieRepository: MovieRepository) {

    fun getMovies(page: Int) = movieRepository.getMovies(page)
}