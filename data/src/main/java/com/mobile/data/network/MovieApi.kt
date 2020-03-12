package com.mobile.data.network

import com.google.gson.JsonObject
import com.mobile.data.model.MoviesResponse
import com.mobile.domain.Movie
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("authentication/token/new")
    fun createRequestToken(): Single<Response<JsonObject>>

    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Single<Response<JsonObject>>

    @POST("authentication/session/new")
    fun createSession(@Body body: JsonObject) : Single<Response<JsonObject>>



    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Single<Response<MoviesResponse>>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int) : Single<Response<Movie>>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ) : Single<Response<MoviesResponse>>
}