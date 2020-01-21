package com.mobile.moviedatabase

import android.app.Application
import com.mobile.moviedatabase.core.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieDBApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieDBApp)
            modules(appModules)
        }
    }
}