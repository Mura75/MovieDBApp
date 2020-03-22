package com.mobile.data.repository

import com.mobile.data.mapper.MovieMapper
import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieMapper: MovieMapper
): MovieRepository {

    override fun getMovies(page: Int): Single<Pair<Int, List<Movie>>> {
        return movieApi.getPopularMovies(page)
            .flatMap { response ->
                if (response.isSuccessful) {
                    val pages = response.body()?.totalPages ?: 0
                    val list = response.body()?.results?.map { movieMapper.to(it) } ?: emptyList()
                    val pair = Pair(pages, list)
                    Single.just(pair)
                } else {
                    Single.error(Throwable(""))
                }
            }
    }

    override fun getMovie(movieId: Int): Single<Movie> {
        return movieApi.getMovie(movieId)
            .flatMap { response ->
                if (response.isSuccessful) {
                    Single.just(response.body())
                } else {
                    Single.error(Throwable(""))
                }
            }
    }
}
