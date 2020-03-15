package com.mobile.data.mapper

import com.mobile.data.model.GenreData
import com.mobile.data.model.MovieData
import com.mobile.domain.Genre
import com.mobile.domain.Mapper
import com.mobile.domain.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() : Mapper<Movie, MovieData> {

    override fun from(model: Movie): MovieData = with(model) {
        return MovieData(
            id = id,
            adult = adult,
            backdropPath = backdropPath,
            genreIds = genreIds,
            genres = genres?.map { GenreData(it.id, it.name) },
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

    override fun to(model: MovieData): Movie = with(model) {
        return Movie(
            id = id,
            adult = adult,
            backdropPath = backdropPath,
            genreIds = genreIds,
            genres = genres?.map { Genre(it.id, it.name) },
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
}
