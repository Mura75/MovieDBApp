package com.mobile.data.repository

import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    override suspend fun getMovies(page: Int) =
        movieApi.getPopularMovies(page)
            .await()
            .body()
            ?.results ?: emptyList()

    override suspend fun getMovie(movieId: Int): Movie? =
        movieApi.getMovie(movieId)
            .await()
            .body()
}