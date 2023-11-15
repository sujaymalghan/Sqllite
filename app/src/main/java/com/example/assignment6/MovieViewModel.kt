package com.example.assignment6

import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment6.Model.Movie
import com.example.assignment6.Model.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {

    var selectedMovie = mutableStateOf<Movie?>(null)
        private set
    val allMovies: LiveData<List<Movie>> = movieDao.getAllMovies()
    private val _uiMovies = MutableLiveData<List<Movie>>()
    val uiMovies: LiveData<List<Movie>> = _uiMovies

    val scrollToIndex = MutableLiveData<Int?>()
    var showText = mutableStateOf<Boolean>(false)
    var showImage = mutableStateOf<Boolean>(false)

    fun isText() {
        val current = showText.value
        showText.value = !current
        showImage.value=false
    }

    fun isImages(){
        val current = showImage.value
        showImage.value = !current
        showText.value=false
    }
    var selectedoneMovie = mutableStateOf<Movie?>(null)
        private set

fun selectoneMovie(movie:Movie?) {
    selectedoneMovie.value=movie
}


    fun selectMovie(movie: Movie?) {
        selectedMovie.value = movie
    }
    private val _selectedMovies = mutableStateMapOf<Int, Boolean>()
    val selectedMovies: Map<Int, Boolean> get() = _selectedMovies

    init {
        showText.value=false
        showImage.value=false
        allMovies.observeForever { moviesFromDb ->
            _uiMovies.value = moviesFromDb
            _selectedMovies.clear()
            moviesFromDb.forEach { movie ->
                _selectedMovies[movie.id] = false
            }
        }
    }


    fun searchMovie(query: String) {
        val index = uiMovies.value?.indexOfFirst { it.title.contains(query, ignoreCase = true) }
        scrollToIndex.value = index
    }


    fun deleteSelectedCards(id: Int ){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMoviesByIds(id)
        }


    }



    fun toggleCardSelection(id: Int) {
        _selectedMovies[id] = _selectedMovies[id] != true
    }


    fun duplicateMovie(movieId: Int , idx :Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val originalMovie = movieDao.getMovie(movieId)
            originalMovie?.let { movie ->

                val duplicatedMovie = Movie(
                    movieId = movie.movieId,
                    title = movie.title,
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath,
                    popularity = movie.popularity,
                    overview = movie.overview,
                    originalLanguage = movie.originalLanguage,
                    originalTitle = movie.originalTitle,
                    releaseDate = movie.releaseDate
                )
                val dbId = movieDao.insertMovie(duplicatedMovie).toInt()

                withContext(Dispatchers.Main) {
                    val currentList = _uiMovies.value?.toMutableList() ?: mutableListOf()
                    val insertIndex = idx + 1
                    if (insertIndex <= currentList.size) {
                        currentList.add(insertIndex, duplicatedMovie)
                        _uiMovies.value = currentList  // Use 'value' to update on the main thread
                    }
                    System.out.println(_uiMovies.value)


                }
            }
        }
    }

    fun getMovieById(movieId: Int): LiveData<Movie?> {
        return movieDao.getMovieById(movieId)
    }
    private val _currentMovie = MutableLiveData<Movie?>()
    val currentMovie: LiveData<Movie?> = _currentMovie

    fun setCurrentMovie(movie: Movie?) {
        _currentMovie.value = movie

    }

    fun sortByTitle() {
        val sortedList = _uiMovies.value.orEmpty().sortedBy { it.title }
        _uiMovies.postValue(sortedList)
    }


    fun sortByRating() {
        val sortedList = _uiMovies.value.orEmpty().sortedBy{ it.voteAverage }
        _uiMovies.postValue(sortedList)
    }



}
