package com.mobile.moviedatabase.data.models


import com.google.gson.annotations.SerializedName
import com.mobile.moviedatabase.data.models.Movie

data class MoviesResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<Movie>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)