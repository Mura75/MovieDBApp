package com.mobile.moviedatabase.core.di

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.mobile.data.networkModule
import com.mobile.data.repositoryModule
import com.mobile.moviedatabase.features.login.AuthViewModel
import com.mobile.moviedatabase.features.movies.detail.MovieDetailViewModel
import com.mobile.moviedatabase.features.movies.favorite.FavoriteMoviesViewModel
import com.mobile.moviedatabase.features.movies.list.MovieListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthViewModel(userRepository = get()) }
    viewModel { MovieListViewModel(movieRepository = get()) }
    viewModel { MovieDetailViewModel(movieRepository = get()) }
    viewModel { FavoriteMoviesViewModel(moviesRepository = get()) }
}

val appModules = listOf(networkModule, repositoryModule, viewModelModule)

@GlideModule
class MovieAppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}