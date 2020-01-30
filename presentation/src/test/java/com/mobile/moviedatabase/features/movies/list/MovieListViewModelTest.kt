package com.mobile.moviedatabase.features.movies.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.Movie
import com.mobile.domain.interactor.GetMoviesInteractor
import com.mobile.moviedatabase.CoroutinesTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var observer: Observer<MovieListViewModel.State>

    @Mock
    lateinit var getMoviesInteractor: GetMoviesInteractor

    lateinit var lifecycle: Lifecycle

    lateinit var moviesListViewModel: MovieListViewModel

    private val moviesList = listOf(
        Movie(id = 1, adult = false, popularity = 9.0, title = "Load of the ring")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        moviesListViewModel = MovieListViewModel(getMoviesInteractor)
        moviesListViewModel.liveData.observeForever(observer)
    }

    @Test
    fun `load movies first page`() {
        val pair: Pair<Int, List<Movie>> = Pair(1, moviesList)
        runBlocking {
            `when`(getMoviesInteractor.getMovies(page = 1)).thenReturn(pair)
            moviesListViewModel.loadMovies(page = 1)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.Result(
                totalPage = pair.first,
                list = pair.second)
            )
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.HideLoading)
        }
    }

    @Test
    fun `load movies second page`() {
        val pair: Pair<Int, List<Movie>> = Pair(2, moviesList)
        runBlocking {
            `when`(getMoviesInteractor.getMovies(page = 2)).thenReturn(pair)
            moviesListViewModel.loadMovies(page = 2)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.Result(
                totalPage = pair.first,
                list = pair.second)
            )
            inOrder(observer).verify(observer).onChanged(MovieListViewModel.State.HideLoading)
        }
    }

    @After
    fun tearDown() {
    }

}