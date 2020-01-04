package com.mobile.moviedatabase.core.network

import com.google.gson.JsonObject
import com.mobile.moviedatabase.data.models.Movie
import com.mobile.moviedatabase.data.models.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("authentication/token/new")
    fun createRequestToken(): Deferred<Response<JsonObject>>

    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Deferred<Response<JsonObject>>

    @POST("authentication/session/new")
    fun createSession(@Body body: JsonObject) : Deferred<Response<JsonObject>>



    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Deferred<Response<MoviesResponse>>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int) : Deferred<Response<Movie>>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ) : Deferred<Response<MoviesResponse>>
}