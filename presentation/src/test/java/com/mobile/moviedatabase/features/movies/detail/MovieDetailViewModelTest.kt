package com.mobile.moviedatabase.features.movies.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.Movie
import com.mobile.domain.interactor.MovieDetailInteractor
import com.mobile.moviedatabase.CoroutinesTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var observer: Observer<MovieDetailViewModel.State>

    @Mock
    lateinit var movieDetailInteractor: MovieDetailInteractor

    lateinit var lifecycle: Lifecycle

    lateinit var movieDetailViewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        movieDetailViewModel = MovieDetailViewModel(movieDetailInteractor)
        movieDetailViewModel.liveData.observeForever(observer)
    }

    @Test
    fun `get single movie detail`() {
        val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        runBlocking {
            `when`(movieDetailInteractor.getMovie(1)).thenReturn(movie)
            movieDetailViewModel.getMovieDetail(1)
            inOrder(observer).verify(observer, times(1))
                .onChanged(MovieDetailViewModel.State.ShowLoading)
            Thread.sleep(1000)
            inOrder(movieDetailInteractor).verify(movieDetailInteractor).getMovie(1)
            inOrder(observer).verify(observer, times(1))
                .onChanged(MovieDetailViewModel.State.Result(movie))
            inOrder(observer).verify(observer, times(1))
                .onChanged(MovieDetailViewModel.State.HideLoading)
        }
    }

    @After
    fun tearDown() {
        reset(observer)
        reset(lifecycleOwner)
        reset(movieDetailInteractor)
        Dispatchers.resetMain()
    }
}