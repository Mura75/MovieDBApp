package com.mobile.data.module

import android.content.Context
import com.mobile.data.storage.LocalPrefStorageImpl
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class StorageModule @Inject constructor(private val context: Context) {

    @Singleton
    @Provides
    fun provideLocalPrefStorage(): LocalPrefStorageImpl = LocalPrefStorageImpl(context = context)

}