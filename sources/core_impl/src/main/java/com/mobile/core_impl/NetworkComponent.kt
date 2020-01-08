package com.mobile.core_impl

import com.mobile.core_api.mediator.AppProvider
import com.mobile.core_api.network.NetworkProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AppProvider::class],
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider