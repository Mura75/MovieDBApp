package com.mobile.domain.interactor

import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepositoryImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class GetMoviesInteractorTest {

    private val movieRepository = MovieRepositoryImplTest()

    private lateinit var getMoviesInteractor: GetMoviesInteractor

    @Before
    fun initTest() {
        getMoviesInteractor = GetMoviesInteractor(movieRepository)
    }

    @Test
    fun getMovies() {
        runBlocking {
            val assertList = listOf(Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring"))
            val movies = getMoviesInteractor.getMovies(1)
            assertEquals(movies, Pair(1, assertList))
        }
    }
}