package com.mobile.domain.interactor

import com.mobile.domain.repository.MovieRepository

class MovieDetailInteractor(private val movieRepository: MovieRepository) {

    suspend fun getMovie(id: Int) = movieRepository.getMovie(id)
}