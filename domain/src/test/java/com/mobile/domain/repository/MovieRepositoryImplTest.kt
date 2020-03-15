package com.mobile.domain.repository

import com.mobile.domain.Movie
import io.reactivex.Single

class MovieRepositoryImplTest : MovieRepository {

    override fun getMovies(page: Int): Single<Pair<Int, List<Movie>>> {
        val movies = listOf(
            Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        )
        val pair = Pair(1, movies)
        return Single.just(pair)
    }

    override fun getMovie(movieId: Int): Single<Movie> {
        val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        return Single.just(movie)
    }
}