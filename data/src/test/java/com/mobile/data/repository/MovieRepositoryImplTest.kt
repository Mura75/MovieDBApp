package com.mobile.data.repository

import com.mobile.data.mapper.MovieMapper
import com.mobile.data.model.MovieData
import com.mobile.data.model.MoviesResponse
import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

class MovieRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var movieApi: MovieApi

    lateinit var movieRepository: MovieRepository

    lateinit var movieMapper: MovieMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieMapper = MovieMapper()
        movieRepository = MovieRepositoryImpl(movieApi, movieMapper)
    }

    @Test
    fun `get movies list`() {
        val movieList = listOf(Movie(id = 1))
        val movieDataList = listOf(MovieData(id = 1))
        val response = Response.success(MoviesResponse(1, movieDataList, 1, 1))
        val deferred = CompletableDeferred(response)
        `when`(movieApi.getPopularMovies(1))
            .thenReturn(deferred)

        runBlocking {
            Assert.assertEquals(movieRepository.getMovies(1), Pair(1, movieList))
        }
    }

    @Test
    fun `get movie by id`() {
        val movie = Movie(id = 1)
        val response = Response.success(Movie(id = 1))
        val deferred = CompletableDeferred(response)
        `when`(movieApi.getMovie(1))
            .thenReturn(deferred)

        runBlocking {
            Assert.assertEquals(movieRepository.getMovie(1), movie)
        }
    }
}