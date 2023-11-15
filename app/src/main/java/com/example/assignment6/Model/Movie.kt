package com.example.assignment6.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movies_list")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Float,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "popularity") val popularity: Float,
    val overview: String,
    @ColumnInfo(name = "original_language") val originalLanguage: String,
    @ColumnInfo(name = "original_title") val originalTitle: String,
    @ColumnInfo(name = "release_date") val releaseDate: String
)
