package com.example.assignment6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignment6.Model.MovieDao

class MovieViewModelFactory(private val movieDao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
