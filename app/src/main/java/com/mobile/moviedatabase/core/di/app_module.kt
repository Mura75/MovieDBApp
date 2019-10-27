package com.mobile.moviedatabase.core.di

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mobile.moviedatabase.BuildConfig
import com.mobile.moviedatabase.R
import com.mobile.moviedatabase.core.exceptions.NoConnectionException
import com.mobile.moviedatabase.core.network.MovieApi
import com.mobile.moviedatabase.features.movies.list.MovieListViewModel
import com.mobile.moviedatabase.features.movies.data.MovieRepository
import com.mobile.moviedatabase.features.movies.data.MovieRepositoryImpl
import com.mobile.moviedatabase.core.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.annotation.GlideModule
import com.mobile.moviedatabase.features.movies.detail.MovieDetailViewModel


val networkModule = module {
    single { createGson() }
    single { createLoggingInterceptor() }
    single(named("auth")) { createAuthInterceptor() }
    single(named("connection")) { createConnectionCheckerInterceptor(context = get()) }
    single {
        createHttpClient(
            httpLoggingInterceptor = get(),
            connectionCheckerInterceptor = get(named("connection")),
            authInterceptor = get(named("auth"))
        )
    }
    single { createApiService(okHttpClient = get(), gson = get()) }
}

val repositoryModule = module {
    single { MovieRepositoryImpl(movieApi = get()) as MovieRepository }
}

val viewModelModule = module {
    viewModel { MovieListViewModel(movieRepository = get()) }
    viewModel { MovieDetailViewModel(movieRepository = get()) }
}

val appModules = listOf(networkModule, repositoryModule, viewModelModule)

private fun createApiService(okHttpClient: OkHttpClient, gson: Gson): MovieApi {
    return Retrofit.Builder()
        .baseUrl(AppConstants.MOVIE_API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(MovieApi::class.java)
}

private fun createHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    connectionCheckerInterceptor: Interceptor,
    authInterceptor: Interceptor
): OkHttpClient {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(connectionCheckerInterceptor)
        .addInterceptor(authInterceptor)
    if (BuildConfig.DEBUG) {
        okHttpClient.addInterceptor(httpLoggingInterceptor)
    }
    return okHttpClient.build()
}

private fun createGson() = GsonBuilder().create()

private fun createLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message)}
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun createConnectionCheckerInterceptor(context: Context): Interceptor {
    return Interceptor { chain: Interceptor.Chain ->
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        val isConnected = netInfo != null && netInfo.isConnected
        if (!isConnected) {
            throw NoConnectionException(R.string.no_network)
        } else {
            chain.proceed(chain.request())
        }
    }
}

private fun createAuthInterceptor(): Interceptor {
    return Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", AppConstants.API_KEY)
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }
}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}