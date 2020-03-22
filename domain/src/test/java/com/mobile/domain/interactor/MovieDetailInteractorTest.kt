package com.mobile.domain.interactor

import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepositoryImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieDetailInteractorTest {

    private val movieRepository = MovieRepositoryImplTest()

    private lateinit var movieDetailInteractor: MovieDetailInteractor

    @Before
    fun initTest() {
        movieDetailInteractor = MovieDetailInteractor(movieRepository)
    }

    @Test
    fun `get movie`() {
        val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        movieDetailInteractor.getMovie(1)
            .test()
            .assertResult(movie)
            .assertNoErrors()
    }

    @Test
    fun `get movie failed`() {
        val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        movieDetailInteractor.getMovie(1)
            .test()
            .assertResult(movie)
            .assertNoErrors()
            .assertValue(movie)
    }
}