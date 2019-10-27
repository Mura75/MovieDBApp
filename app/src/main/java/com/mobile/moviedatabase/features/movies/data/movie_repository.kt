package com.mobile.moviedatabase.features.movies.data

import com.mobile.moviedatabase.core.network.MovieApi

interface MovieRepository {
    suspend fun getMovies(page: Int): MoviesResponse?
    suspend fun getMovie(movieId: Int): Movie?
}

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    override suspend fun getMovies(page: Int) =
        movieApi.getPopularMovies(page)
            .await()
            .body()

    override suspend fun getMovie(movieId: Int): Movie? =
        movieApi.getMovie(movieId)
            .await()
            .body()
}