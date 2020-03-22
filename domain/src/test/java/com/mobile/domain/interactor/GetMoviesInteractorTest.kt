package com.mobile.domain.interactor

import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepositoryImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMoviesInteractorTest {

    private val movieRepository = MovieRepositoryImplTest()

    private lateinit var getMoviesInteractor: GetMoviesInteractor

    @Before
    fun initTest() {
        getMoviesInteractor = GetMoviesInteractor(movieRepository)
    }

    @Test
    fun getMovies() {
        val assertList = listOf(
            Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        )
        getMoviesInteractor.getMovies(1)
            .test()
            .assertResult(Pair(1, assertList))
            .assertNoErrors()
    }
}