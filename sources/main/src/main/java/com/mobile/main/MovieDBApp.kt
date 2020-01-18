package com.mobile.main

import android.app.Application
import android.content.Context
import com.mobile.core_api.mediator.AppProvider
import com.mobile.core_api.mediator.AppWithFacade
import com.mobile.core_api.mediator.ProvidersFacade

class MovieDBApp : Application(), ProvidersFacade {

    override fun onCreate() {
        super.onCreate()
    }

    override fun provideContext(): Context {
        return applicationContext
    }

}