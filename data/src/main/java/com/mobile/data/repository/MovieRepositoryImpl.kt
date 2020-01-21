package com.mobile.data.repository

import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    override suspend fun getMovies(page: Int): Pair<Int, List<Movie>> {
        val response = movieApi.getPopularMovies(page)
            .await()
            .body()
        val pages = response?.totalPages ?: 0
        val list = response?.results ?: emptyList()
        return Pair(pages, list)
    }

    override suspend fun getMovie(movieId: Int): Movie? =
        movieApi.getMovie(movieId)
            .await()
            .body()
}