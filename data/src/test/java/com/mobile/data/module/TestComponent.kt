package com.mobile.data.module

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestNetworkModule::class
    ]
)
interface TestComponent {


}