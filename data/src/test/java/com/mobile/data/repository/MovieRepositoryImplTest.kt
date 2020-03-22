package com.mobile.data.repository

import com.mobile.data.mapper.MovieMapper
import com.mobile.data.model.MovieData
import com.mobile.data.model.MoviesResponse
import com.mobile.data.network.MovieApi
import com.mobile.domain.Movie
import com.mobile.domain.repository.MovieRepository
import io.reactivex.Single
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.*

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
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

        `when`(movieApi.getPopularMovies(1))
            .thenReturn(Single.just(response))

        movieRepository.getMovies(1)
            .test()
            .assertResult(Pair(1, movieList))
            .assertNoErrors()
    }

    @Test
    fun `get movie by id`() {
        val movie = Movie(id = 1)
        val response = Response.success(Movie(id = 1))

        `when`(movieApi.getMovie(1))
            .thenReturn(Single.just(response))

        movieApi.getMovie(1)
            .test()
            .assertResult(response)
            .assertNoErrors()
    }

    @After
    fun tearDown() {
        reset(movieApi)
    }
}