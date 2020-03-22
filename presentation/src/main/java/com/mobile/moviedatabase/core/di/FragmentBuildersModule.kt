package com.mobile.telecomapp.di

import com.mobile.moviedatabase.features.movies.detail.MovieDetailsFragment
import com.mobile.moviedatabase.features.movies.list.MoviesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesListFragment(): MoviesListFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment

}