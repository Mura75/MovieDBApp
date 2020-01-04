package com.mobile.moviedatabase.core.network

import com.google.gson.JsonObject
import com.mobile.moviedatabase.features.movies.data.Movie
import com.mobile.moviedatabase.features.movies.data.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("authentication/token/new")
    fun createRequestToken(): Deferred<Response<JsonObject>>

    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Deferred<Response<JsonObject>>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Deferred<Response<MoviesResponse>>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int) : Deferred<Response<Movie>>
}