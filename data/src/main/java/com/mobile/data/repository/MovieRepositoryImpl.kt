package com.mobile.data.repository

import com.mobile.data.mapper.MovieMapper
import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieMapper: MovieMapper
): MovieRepository {

    override suspend fun getMovies(page: Int): Pair<Int, List<Movie>> {
        val response = movieApi.getPopularMovies(page)
            .await()
            .body()
        val pages = response?.totalPages ?: 0
        val list = response?.results?.map { movieMapper.to(it) } ?: emptyList()
        return Pair(pages, list)
    }

    override suspend fun getMovie(movieId: Int): Movie? =
        movieApi.getMovie(movieId)
            .await()
            .body()
}