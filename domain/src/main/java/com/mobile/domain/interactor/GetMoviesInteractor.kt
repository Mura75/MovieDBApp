package com.mobile.domain.interactor

import com.mobile.domain.repository.MovieRepository

class GetMoviesInteractor(private val movieRepository: MovieRepository) {

    suspend fun getMovies(page: Int) = movieRepository.getMovies(page)
}