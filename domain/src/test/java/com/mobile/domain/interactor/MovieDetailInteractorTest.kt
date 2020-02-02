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
    fun getMovie() {
        runBlocking {
            val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
            assertEquals(movieDetailInteractor.getMovie(1), movie)
        }
    }
}