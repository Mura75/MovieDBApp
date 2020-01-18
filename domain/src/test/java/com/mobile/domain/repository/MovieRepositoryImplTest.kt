package com.mobile.domain.repository

import com.mobile.domain.Movie

class MovieRepositoryImplTest : MovieRepository {

    override suspend fun getMovies(page: Int): Pair<Int, List<Movie>> {
        val movies = listOf(Movie(id = 1, adult = false, popularity = 9.0, title = "Load of the ring"))
        return Pair(1, movies)
    }

    override suspend fun getMovie(movieId: Int): Movie? {
        return Movie(id = 1, adult = false, popularity = 9.0, title = "Load of the ring")
    }
}