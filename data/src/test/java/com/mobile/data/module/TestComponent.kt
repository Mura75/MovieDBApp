package com.mobile.data.module

import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestNetworkModule::class
    ]
)
interface TestComponent {


}