package com.example.assignment6


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment6.Model.Movie

class AboutMeViewModel : ViewModel() {
    private val _showHomeText = MutableLiveData(false)
    val showHomeText: LiveData<Boolean> = _showHomeText

    private val _selectedMovie = MutableLiveData<Movie?>(null)
    val selectedM: MutableLiveData<Movie?> = _selectedMovie

    fun setSelectedMovie(movieId: Movie?) {
        _selectedMovie.value = movieId
    }
    fun toggleShowHomeText() {
        _showHomeText.value = !(_showHomeText.value ?: false)
    }


    private val _showBottomBar = MutableLiveData(false)
    val showBottomBar: LiveData<Boolean> = _showBottomBar

    // Function to toggle the value
    fun toggleShowBottomBar() {
        _showBottomBar.value = !(_showBottomBar.value ?: false)
    }
}
