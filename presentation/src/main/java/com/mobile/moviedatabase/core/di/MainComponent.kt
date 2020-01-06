package com.mobile.moviedatabase.core.di

import android.app.Application
import com.mobile.data.module.NetworkModule
import com.mobile.data.module.RepositoryModule
import com.mobile.data.module.StorageModule
import com.mobile.moviedatabase.MovieDBApp
import com.mobile.telecomapp.di.ActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class,
        StorageModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface MainComponent : AndroidInjector<MovieDBApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun repositoryModule(repositoryModule: RepositoryModule): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun storageModule(storageModule: StorageModule): Builder

        fun build(): MainComponent
    }
}