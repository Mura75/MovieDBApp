package com.mobile.domain.repository

import com.mobile.domain.Movie

interface MovieRepository {
    suspend fun getMovies(page: Int): List<Movie>?
    suspend fun getMovie(movieId: Int): Movie?
}