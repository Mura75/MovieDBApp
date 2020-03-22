package com.mobile.domain.repository

import com.mobile.domain.Movie
import io.reactivex.Single

interface MovieRepository {
    fun getMovies(page: Int): Single<Pair<Int, List<Movie>>>
    fun getMovie(movieId: Int): Single<Movie>
}