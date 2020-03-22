package com.mobile.moviedatabase.features.movies.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.mobile.domain.Movie
import com.mobile.domain.interactor.GetMoviesInteractor
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
class MovieListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var observer: Observer<MovieListViewModel.State>

    @Mock
    lateinit var getMoviesInteractor: GetMoviesInteractor

    lateinit var lifecycle: Lifecycle

    lateinit var moviesListViewModel: MovieListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        moviesListViewModel = MovieListViewModel(getMoviesInteractor)
        moviesListViewModel.liveData.observeForever(observer)
    }

    @Test
    fun `load movies first page with successs`() {
        val moviesList = listOf(
                Movie(id = 1, adult = false, popularity = 9.0, title = "Lord of the ring")
        )
        val pair: Pair<Int, List<Movie>> = Pair(1, moviesList)
        `when`(getMoviesInteractor.getMovies(page = 1))
            .thenReturn(Single.just(pair))

        moviesListViewModel.loadMovies(page = 1)

        verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
        verify(observer).onChanged(
            MovieListViewModel.State.Result(
                totalPage = pair.first,
                list = pair.second
            )
        )
        verify(observer).onChanged(MovieListViewModel.State.HideLoading)
    }

    @Test
    fun `load movies first page with error`() {
        `when`(getMoviesInteractor.getMovies(page = 1))
            .thenReturn(Single.error(Throwable("connection error")))

        moviesListViewModel.loadMovies(page = 1)

        verify(observer).onChanged(MovieListViewModel.State.ShowLoading)
        verify(observer).onChanged(MovieListViewModel.State.HideLoading)
        verify(observer).onChanged(MovieListViewModel.State.Error("connection error"))
    }

    @After
    fun tearDown() {
        reset(lifecycleOwner)
        reset(getMoviesInteractor)
        reset(observer)
        Dispatchers.resetMain()
    }
}