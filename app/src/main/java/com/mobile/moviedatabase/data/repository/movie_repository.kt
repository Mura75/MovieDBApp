package com.mobile.moviedatabase.data.repository

import com.mobile.moviedatabase.core.network.MovieApi
import com.mobile.moviedatabase.data.models.Movie
import com.mobile.moviedatabase.data.models.MoviesResponse

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