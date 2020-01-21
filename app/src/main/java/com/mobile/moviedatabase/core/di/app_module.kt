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
import com.mobile.moviedatabase.data.repository.MovieRepository
import com.mobile.moviedatabase.data.repository.MovieRepositoryImpl
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
import com.mobile.moviedatabase.data.repository.UserRepository
import com.mobile.moviedatabase.data.repository.UserRepositoryImpl
import com.mobile.moviedatabase.data.storage.LocalPrefStorage
import com.mobile.moviedatabase.features.login.AuthViewModel
import com.mobile.moviedatabase.features.movies.detail.MovieDetailViewModel
import com.mobile.moviedatabase.features.movies.favorite.FavoriteMoviesViewModel
import okhttp3.Response


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
    single { LocalPrefStorage(context = get()) }
}

val repositoryModule = module {
    single { MovieRepositoryImpl(movieApi = get()) as MovieRepository }
    single { UserRepositoryImpl(movieApi = get(), localPrefStorage = get()) as UserRepository }
}

val viewModelModule = module {
    viewModel { AuthViewModel(userRepository = get()) }
    viewModel { MovieListViewModel(movieRepository = get()) }
    viewModel { MovieDetailViewModel(movieRepository = get()) }
    viewModel { FavoriteMoviesViewModel(moviesRepository = get()) }
    //viewModel { LoginViewModel(api = get()) }
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


//Okhttp client
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

//Gson for json parsing
private fun createGson() = GsonBuilder().create()


//Logging interceptor
private fun createLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.d("OkHttp", message)
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

//Network connection checker
private fun createConnectionCheckerInterceptor(context: Context): Interceptor {
    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            val isConnected = netInfo != null && netInfo.isConnected
            if (!isConnected) {
                throw NoConnectionException(R.string.no_network)
            } else {
                return chain.proceed(chain.request())
            }
        }
    }
}

//Auth interceptor for Service API
private fun createAuthInterceptor(): Interceptor {
    return  object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .build()
            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            return chain.proceed(newRequest)
        }
    }
}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}