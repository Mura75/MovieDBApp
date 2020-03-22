package com.mobile.moviedatabase.features.movies.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.Movie
import com.mobile.domain.interactor.MovieDetailInteractor
import com.mobile.moviedatabase.CoroutinesTestRule
import com.mobile.moviedatabase.RxRule
import io.reactivex.Single
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
    val rxRule = RxRule()

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
    fun `get single movie detail success`() {
        val movie = Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        `when`(movieDetailInteractor.getMovie(1))
            .thenReturn(Single.just(movie))

        movieDetailViewModel.getMovieDetail(1)
        verify(observer).onChanged(MovieDetailViewModel.State.ShowLoading)
        verify(observer).onChanged(MovieDetailViewModel.State.Result(movie))
        verify(observer).onChanged(MovieDetailViewModel.State.HideLoading)
    }

    @Test
    fun `get single movie detail error`() {
        `when`(movieDetailInteractor.getMovie(1))
            .thenReturn(Single.error(Throwable("connection error")))

        movieDetailViewModel.getMovieDetail(1)
        verify(observer).onChanged(MovieDetailViewModel.State.ShowLoading)
        verify(observer).onChanged(MovieDetailViewModel.State.HideLoading)
        verify(observer).onChanged(MovieDetailViewModel.State.Error("connection error"))
    }

    @After
    fun tearDown() {
        reset(observer)
        reset(lifecycleOwner)
        reset(movieDetailInteractor)
        Dispatchers.resetMain()
    }
}