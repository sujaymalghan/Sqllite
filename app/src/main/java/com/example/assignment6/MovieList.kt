package com.example.assignment6

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment6.Model.DatabaseBuilder
import com.example.assignment6.Model.Movie
import com.example.assignment6.ui.theme.Assignment5Theme

class MovieList : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Assignment5Theme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                val movieView: AboutMeViewModel by viewModels()
                var selectedMovieId = movieView.selectedM.observeAsState().value
                val appDatabase = DatabaseBuilder.getInstance(this)
                val movieViewModel: MovieViewModel by viewModels {
                    MovieViewModelFactory(appDatabase.movieDao())
                }
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


                val showBottomBar = remember { mutableStateOf(true) } // State to track visibility of bottom bar

                Scaffold(
                    topBar = {

                        val showBackButton = currentRoute != "movieDetails/{movieId}"
                        if (selectedMovieId != null) {

                            TopAppBar(
                                title = {
                                    IconButton(onClick = {
                                        movieView.setSelectedMovie(null)
                                    }) {
                                        val cancelIcon = painterResource(id = R.drawable.cancel)
                                        Image(
                                            painter = cancelIcon,
                                            contentDescription = "Cancel",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                colors  = TopAppBarDefaults.smallTopAppBarColors( Color(0xFFE8F5E9)),
                                actions = {


                                    IconButton(onClick = {
                                        movieViewModel.duplicateMovie(
                                        selectedMovieId!!.movieId, selectedMovieId!!.id)
                                        movieView.setSelectedMovie(null)

                                    }) {
                                        val duplicateIcon = painterResource(id = R.drawable.duplicate)
                                        Image(
                                            painter = duplicateIcon,
                                            contentDescription = "Duplicate",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    IconButton(onClick = { movieViewModel.deleteSelectedCards(
                                        selectedMovieId!!.id)
                                        movieView.setSelectedMovie(null)

                                    }

                                    ) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete",
                                            tint=Color.Black)
                                    }
                                }
                            )
                        } else if (showBackButton) {
                            TaskAppBar(name = "Movie List",movieViewModel)
                        }
                        else{

                            Taskbar("Single Movie", onShareClick = {

                            } , movieViewModel)
                        }


                             },


                    content = { innerPadding ->

                        val appDatabase = DatabaseBuilder.getInstance(this)
                        val movieViewModel: MovieViewModel by viewModels {
                            MovieViewModelFactory(appDatabase.movieDao())
                        }

                        NavHost(
                            navController = navController,
                            startDestination = "movieList",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = "movieList") {

                                MovieListScreen(movieViewModel, navController,movieView)
                            }
                            composable("movieDetails/{movieId}",
                                arguments = listOf(navArgument("movieId") { type = NavType.IntType }))
                            { backStackEntry ->

                                val movieId = backStackEntry.arguments?.getInt("movieId")

                                MovieDetails(movieId, movieViewModel)
                            }
                            composable(
                                "oneMovie/{movietitle}",
                                arguments = listOf(navArgument("movietitle") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val movietitle = backStackEntry.arguments?.getString("movietitle") ?: return@composable
                                OneMovieScreen(movietitle)
                            }
                            composable(
                                "oneMovie/{movieid}",
                                arguments = listOf(navArgument("movieid") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val movieid = backStackEntry.arguments?.getInt("movieid")
                                if (movieid != null) {
                                    oneMovieImage(movieid,movieViewModel)
                                }
                            }



                        }
                    }
                )
            }
        }
    }
}


@Composable
fun oneMovieImage(movieid: Int, movieViewModel: MovieViewModel) {
    val testmovies by movieViewModel.uiMovies.observeAsState(initial = emptyList())

    val movie = testmovies.find { it.movieId == movieid }

    val imageUrl = movie?.posterPath?.let { "https://image.tmdb.org/t/p/w400/$it" }

    if (imageUrl != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp).verticalScroll(rememberScrollState())
        ) {
            MovieImage3(imageUrl = imageUrl)
        }
    }
}

@Composable
fun OneMovieScreen(movietitle: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF800000))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = movietitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(name: String, movieViewModel: MovieViewModel,showBackButton: Boolean = true) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density
    val myShadowSize = context.resources.getDimension(R.dimen.shadow_size) / density
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var ismovieselect = movieViewModel.selectedMovie.value != null

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            titleContentColor = Color.Black,
            navigationIconContentColor = Color(0xFF36454F),
            containerColor = Color(0xFFF5F5DC)
        ),
        modifier = Modifier.shadow(myShadowSize.dp),
        title = {
            if (isSearching)
            {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        containerColor= Color.Green,
                        textColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        movieViewModel.searchMovie(searchQuery.trim())
                        isSearching = false
                    }),
                    singleLine = true
                )
            } else {
                Text(text = name)
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    (context as? Activity)?.finish()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {

            if (isSearching) {
                IconButton(onClick = {
                    isSearching = false
                    searchQuery = ""
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close search", tint = Color.Black)
                }
            } else {

                IconButton(onClick = {
                    movieViewModel.isText()
                }) {
                    if (movieViewModel.showText.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.lists),
                            contentDescription = "List view",
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.text),
                            contentDescription = "Image view",
                            tint = Color.Black
                        )
                    }

                }
                if (ismovieselect) {
                    IconButton(onClick = {
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                    }
                }

                IconButton(onClick = {
                    movieViewModel.isImages()
                }) {
                    if (movieViewModel.showImage.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.lists),
                            contentDescription = "List view",
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Image view",
                            tint = Color.Black
                        )
                    }
                }

                IconButton(onClick = {
                    isSearching = true
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = Color.Black)
                }

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taskbar(name: String, onShareClick: () -> Unit, movieViewModel: MovieViewModel) {

    val context = LocalContext.current

    val movieobj = movieViewModel.currentMovie.observeAsState()
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            titleContentColor = Color.Black,
            navigationIconContentColor = Color(0xFF36454F),
            containerColor = Color(0xFFF5F5DC)
        ),
        title = { Text(text = name) },
        actions = {
            IconButton(onClick = {
                movieobj?.let { shareMovieDetails(context, it) }
            }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share", tint=Color.Black
                )
            }
        }
    )
}

fun shareMovieDetails(context: Context, movie: State<Movie?>) {
    val shareText = StringBuilder().apply {
        append("Check out this movie: ${movie?.value?.title ?: "N/A"}")
        append("\n Rating: ${
            movie?.value
                ?.voteAverage ?: "N/A"}")
        append("\n Check the Overview: ${movie.value?.overview}")
    }.toString()

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Movie Details"))
}