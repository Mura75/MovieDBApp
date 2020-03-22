package com.mobile.data.module

import com.mobile.data.mapper.MovieMapper
import com.mobile.data.network.MovieApi
import com.mobile.data.repository.MovieRepositoryImpl
import com.mobile.data.repository.UserRepositoryImpl
import com.mobile.data.storage.LocalPrefStorageImpl
import com.mobile.domain.repository.MovieRepository
import com.mobile.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(NetworkModule::class, StorageModule::class))
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieApi: MovieApi,
        movieMapper: MovieMapper
    ): MovieRepository = MovieRepositoryImpl(movieApi = movieApi, movieMapper = movieMapper)

    @Singleton
    @Provides
    fun provideUserRepository(
        movieApi: MovieApi,
        localPrefStorage: LocalPrefStorageImpl
    ): UserRepository = UserRepositoryImpl(movieApi = movieApi, localPrefStorage = localPrefStorage)
}
