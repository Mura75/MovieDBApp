package com.mobile.moviedatabase.core.di

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.mobile.data.networkModule
import com.mobile.data.repositoryModule
import com.mobile.domain.interactor.AuthInteractor
import com.mobile.domain.interactor.GetMoviesInteractor
import com.mobile.domain.interactor.UserExistInteractor
import com.mobile.moviedatabase.features.login.AuthViewModel
import com.mobile.moviedatabase.features.movies.detail.MovieDetailViewModel
import com.mobile.moviedatabase.features.movies.favorite.FavoriteMoviesViewModel
import com.mobile.moviedatabase.features.movies.list.MovieListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthViewModel(authInteractor = get(), userExistInteractor = get()) }
    viewModel { MovieListViewModel(getMoviesInteractor = get()) }
    viewModel { MovieDetailViewModel(movieDetailInteractor = get()) }
    viewModel { FavoriteMoviesViewModel(moviesRepository = get()) }
}

val domainModule = module {
    factory { UserExistInteractor(userRepository = get()) }
    factory { AuthInteractor(userRepository = get()) }
    factory { GetMoviesInteractor(movieRepository = get()) }
}

val appModules = listOf(networkModule, repositoryModule, domainModule, viewModelModule)

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(appModules)
}

@GlideModule
class MovieAppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}