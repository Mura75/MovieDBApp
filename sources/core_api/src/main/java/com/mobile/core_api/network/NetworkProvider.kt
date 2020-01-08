package com.mobile.core_api.network

interface NetworkProvider {

    fun provideApi(): MovieDbMainApi
}