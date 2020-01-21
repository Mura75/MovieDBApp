package com.mobile.data.module

import android.content.Context
import com.mobile.data.network.MovieApi
import com.mobile.data.repository.MovieRepositoryImpl
import com.mobile.data.storage.LocalPrefStorage
import com.mobile.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class StorageModule @Inject constructor(private val context: Context) {

    @Singleton
    @Provides
    fun provideLocalPrefStorage(): LocalPrefStorage = LocalPrefStorage(context = context)

}