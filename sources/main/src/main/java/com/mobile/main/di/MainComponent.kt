package com.mobile.main.di

import com.mobile.core_api.mediator.ProvidersFacade
import com.mobile.main.MainActivity
import dagger.Component

@Component(dependencies = arrayOf(ProvidersFacade::class))
interface MainComponent {
    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent.builder().providersFacade(providersFacade).build()
        }
    }

    fun inject(mainActivity: MainActivity)
}