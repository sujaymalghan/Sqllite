package com.example.assignment6

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment6.Model.DatabaseBuilder
import com.example.assignment6.Model.Movie
import com.example.assignment6.ui.theme.Assignment5Theme


class MovieListtaskthree : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieView: AboutMeViewModel by viewModels()

        movieView.showBottomBar.observe(this) { showBottomBar ->
        setContent {
            Assignment5Theme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
             //   val movieView: AboutMeViewModel by viewModels()
                var selectedMovieId = movieView.selectedM.observeAsState().value
                val appDatabase = DatabaseBuilder.getInstance(this)
                val movieViewModel: MovieViewModel by viewModels {
                    MovieViewModelFactory(appDatabase.movieDao())
                }
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

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
                                        Icon(
                                            Icons.Filled.Delete, contentDescription = "Delete",
                                            tint= Color.Black)
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

                        Button(
                            modifier = Modifier.padding(start = 156.dp, top = 15.dp),
                            onClick = { movieView.toggleShowBottomBar() }) {
                            Text(
                                text = if (movieView.showBottomBar.value == true) "Hide" else "Unhide",
                                style = TextStyle(fontSize = 9.sp)
                            )
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
                    },



                    bottomBar = {
                        if (movieView.showBottomBar.value == true) {
                            MovieBottomBar(movieViewModel)
                        }
                    }                )

            }
        }
    }
    }


}


@Composable
fun MovieBottomBar(movieViewModel: MovieViewModel) {
    BottomAppBar(
        containerColor = Color.LightGray, // Set to your preferred light color
        contentColor = Color.Black
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.5f).clickable { movieViewModel.sortByTitle() }
        ) {

            Icon(painter = painterResource(id = R.drawable.title), contentDescription = "Sort by Title",tint=Color.Black)
            Text(text = "Sort by Title", style = MaterialTheme.typography.titleSmall, color = Color.Black)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.5f).clickable {  movieViewModel.sortByRating()  }
        ) {

            Icon(Icons.Filled.Star , contentDescription = "Sort by Rating")
            Text(text = "Sort by Rating", style = MaterialTheme.typography.titleSmall, color = Color.Black)
        }
    }
}