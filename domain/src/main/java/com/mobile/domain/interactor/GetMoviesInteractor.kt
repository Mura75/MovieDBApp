package com.mobile.domain.interactor

import com.mobile.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesInteractor @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getMovies(page: Int) = movieRepository.getMovies(page)
}