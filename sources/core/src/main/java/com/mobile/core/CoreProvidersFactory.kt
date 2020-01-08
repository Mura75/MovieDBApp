package com.mobile.core

import com.mobile.core_api.mediator.AppProvider
import com.mobile.core_api.network.NetworkProvider
import com.mobile.core_api.viewmodel.ViewModelsProvider
import com.mobile.core_impl.DaggerNetworkComponent
import com.mobile.core_impl.DaggerViewModelComponent

object CoreProvidersFactory {

    fun createNetworkBuilder(appProvider: AppProvider): NetworkProvider {
        return DaggerNetworkComponent.builder().appProvider(appProvider).build()
    }

    fun createViewModelBuilder(): ViewModelsProvider {
        return DaggerViewModelComponent.create()
    }
}