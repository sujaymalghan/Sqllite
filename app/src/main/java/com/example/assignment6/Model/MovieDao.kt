package com.example.assignment6.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies_list")
    fun getAllMovies(): LiveData<List<Movie>>

    @Insert
    fun insertMovie(movie: Movie):Long

    @Query("SELECT * FROM movies_list WHERE movie_id = :movieId LIMIT 1")
    fun getMovieById(movieId: Int): LiveData<Movie?>

    @Query("SELECT * FROM movies_list WHERE movie_id = :movieId LIMIT 1")
    fun getMovie(movieId: Int): Movie?

    @Query("DELETE FROM movies_list WHERE id IN (:movieIds)")
      fun deleteMoviesByIds(movieIds: Int)


}

