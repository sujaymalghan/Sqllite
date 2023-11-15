package com.example.assignment6


import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.assignment6.Model.Movie


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieListScreen(movieViewModel: MovieViewModel, navController: NavController,aboutMeViewModel: AboutMeViewModel) {



    val movies by movieViewModel.uiMovies.observeAsState(initial = emptyList())


    val isTablet = LocalConfiguration.current.screenWidthDp >= 800


    val listState = rememberLazyListState()
    val context = LocalContext.current
    val scrollToIndex by movieViewModel.scrollToIndex.observeAsState()

    var istext = movieViewModel.showText.value
    var isImage = movieViewModel.showImage.value

    LaunchedEffect(scrollToIndex) {
        if (scrollToIndex != null) {
            val index = scrollToIndex!!
            if (index >= 0 && index < movies.size) {

                listState.animateScrollToItem(index,20)
            } else {
                Toast.makeText(context, "Movie not found, scrolled to the first movie.", Toast.LENGTH_SHORT).show()
                listState.animateScrollToItem(0)
            }
            movieViewModel.scrollToIndex.value = null // Reset after handling
        }
    }





    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isTablet) {
            val isMovieSelected = movieViewModel.selectedMovie.value != null

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(if (isMovieSelected) 0.5f else 1f)
                        .fillMaxHeight()
                ) {
                    itemsIndexed(movies, key = { index, movie ->
                        movie.id
                    }){
                            index, movie ->
                        if (istext != null) {
                            MovieCard(
                                movie = movie,
                                uniqueId = movie.id,
                                movieViewModel = movieViewModel,
                                onMovieClick = { movieId ->
                                    val selectedMovie = if (movieViewModel.selectedMovie.value?.id == movieId.toInt())
                                        null
                                    else


                                        movie
                                    movieViewModel.selectMovie(selectedMovie)
                                    aboutMeViewModel.setSelectedMovie(null)
                                },
                                onLongClick = {
                                    aboutMeViewModel.setSelectedMovie(movie)

                                },
                                isTablet = isTablet,
                                isMovieSelected = isMovieSelected,
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = tween(durationMillis = 2000)
                                ),
                                onMenuItemClick = { action, id ->
                                    when (action) {
                                        "duplicate" -> {   movieViewModel.duplicateMovie(movie.movieId, id) }
                                        "delete" -> {movieViewModel.deleteSelectedCards(movie.id)}


                                    }
                                },
                                istext,
                                isImage,navController

                            )
                        }
                    }
                }

                if (isMovieSelected) {

                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                    ) {
                        SelectionContainer {
                        Box(modifier = Modifier.fillMaxSize()) {
                            movieViewModel.selectedMovie.value?.let {
                                MovieDetails(it.movieId, movieViewModel)
                            }
                        }
                    }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                state=listState,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(movies, key = { index, movie ->
                    movie.id
                }) {
                        index, movie ->
                    if (istext != null) {
                        MovieCard(
                            movie = movie,
                            uniqueId = movie.id,
                            movieViewModel = movieViewModel,
                            onMovieClick = { movieId ->

                                navController.navigate("movieDetails/${movie.movieId}")
                                aboutMeViewModel.setSelectedMovie(null)
                            },
                            onLongClick = {
                                aboutMeViewModel.setSelectedMovie(movie)
                            },
                            isTablet = false,
                            isMovieSelected = false,
                            modifier = Modifier.animateItemPlacement(
                                animationSpec = tween(durationMillis = 2000)
                            ),
                            onMenuItemClick = { action, id ->
                                when (action) {
                                    "duplicate" -> {movieViewModel.duplicateMovie(movie.movieId, id)    }
                                    "delete" -> { movieViewModel.deleteSelectedCards(movie.id)}
                                }
                            },
                            istext = istext, isImage, navController
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieCard(
    movie: Movie?, uniqueId: Int, movieViewModel: MovieViewModel, onMovieClick: (String) -> Unit,
    onLongClick: (Movie) -> Unit,

    isTablet: Boolean,
    isMovieSelected: Boolean, modifier: Modifier = Modifier,
    onMenuItemClick: (String, Int) -> Unit,
    istext: Boolean,
    isImage: Boolean,
    navController: NavController


) {


    val screenWidthcard = LocalConfiguration.current.screenWidthDp.dp
    val adjustedPadding = if (screenWidthcard >= 800.dp) 18.dp else 5.dp
    val adjustedbottomPadding = if (screenWidthcard >= 800.dp ) 14.dp else 8.dp
    val isChecked = movieViewModel.selectedMovies[uniqueId] ?: false
    val expanded = remember { mutableStateOf(false) }

    if (istext && movie != null && !isImage) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(Color(0xFF800000))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = {
                            if (movie != null) {
                                navController.navigate("oneMovie/${movie.title}")
                            }
                        },
                        onLongClick = { movie?.let(onLongClick) }
                    )
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (isImage && movie != null && !istext) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        if (movie != null) {
                            navController.navigate("oneMovie/${movie.movieId}")
                        }
                    },
                    onLongClick = { movie?.let(onLongClick) }
                )

            ,
            colors = CardDefaults.cardColors(Color(0xFF800000))

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                val imageUrl = "https://image.tmdb.org/t/p/w185/${movie.posterPath}"
                MovieImage(imageUrl = imageUrl)
            }
        }
    }


    if (movie != null && !istext && !isImage) {

        if (movie.voteAverage < 8.0) {

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFECF0F1),
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = adjustedbottomPadding,
                            start = adjustedPadding,
                            end = adjustedPadding
                        )
                        .combinedClickable(
                            onClick = {
                                onMovieClick(movie?.id.toString())

                            },
                            onLongClick = {
                                onLongClick(movie)
                            }
                        ),

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { movieViewModel.toggleCardSelection(uniqueId) },
                                    modifier = Modifier.align(Alignment.Top)
                                )
                                IconButton(onClick = { expanded.value = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More",
                                        tint = Color.Black

                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false },
                                    modifier = Modifier.background(Color(0xFFA5D6A7))
                                ) {

                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.duplicate2),
                                                contentDescription = "Duplicate"
                                            )
                                        },
                                        text = { Text("Duplicate",color = Color(0xFFE0E0E0) ) },
                                        onClick = {
                                            onMenuItemClick("duplicate", uniqueId)
                                            expanded.value = false
                                        },
                                        modifier = Modifier.background(Color(0xFF2196F3)) // Lighter blue background
                                    )
                                    Spacer(modifier = modifier.padding(top=14.dp) )
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(R.drawable.delete),
                                                contentDescription = "Delete"
                                            )
                                        },


                                        text = { Text("Delete",color = Color(0xFFE0E0E0) ) },
                                        onClick = {
                                            onMenuItemClick("delete", uniqueId)
                                            expanded.value = false
                                        },
                                        modifier = Modifier.background(Color(0xFF2196F3)) // Lighter blue background

                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    movie?.title?.let {
                                        val screenWidthtitle =
                                            LocalConfiguration.current.screenWidthDp.dp
                                        val votesize =
                                            if (screenWidthtitle <= 800.dp) 20.sp else 30.sp

                                        Text(
                                            text = it,
                                            textAlign = TextAlign.Center,
                                            style =   TextStyle(fontSize = votesize ),
                                            color = Color(0xFF2C3E50),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    movie?.voteAverage?.let {
                                        val screenWidthvote =
                                            LocalConfiguration.current.screenWidthDp.dp
                                        val votesize =
                                            if (screenWidthvote <= 800.dp) 14.sp else 24.sp
                                        Text(
                                            text = "Rating : $it",
                                            style = TextStyle(fontSize = votesize),
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            color=Color(0xFF2C3E50)
                                            ,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            if (movie != null) {
                                movie.overview?.let { overviewText ->
                                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                                    val overviewtext = if (screenWidth <= 800.dp) 14.sp
                                    else if (screenWidth>=800.dp && isMovieSelected) 20.sp
                                    else 24.sp

                                    Text(
                                        text = overviewText,
                                        style = TextStyle.Default.copy(fontSize = overviewtext, color = Color(0xFF2C3E50)),
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis
                                        )
                                }

                            }
                        }
                        val imageUrl = "https://image.tmdb.org/t/p/w185/${movie.posterPath!!}"

                       MovieImage(imageUrl = imageUrl)
                    }
                }


        } else {

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFECF0F1),
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = adjustedbottomPadding,
                            start = adjustedPadding,
                            end = adjustedPadding
                        )
                        .combinedClickable(
                            onClick = { onMovieClick(movie?.id.toString()) },
                            onLongClick = {
                                onLongClick(movie)
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        val imageUrl = "https://image.tmdb.org/t/p/w185/${movie.posterPath!!}"

                        MovieImage(imageUrl = imageUrl)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (movie != null) {
                                    movie.title?.let {
                                        val screenWidthtitle =
                                            LocalConfiguration.current.screenWidthDp.dp
                                        val votesize =
                                            if (screenWidthtitle <= 800.dp) 20.sp else 30.sp

                                        Text(
                                            text = it,
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            style = TextStyle(
                                                fontSize = votesize
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color(0xFF2C3E50)
                                        )
                                    }
                                }
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { movieViewModel.toggleCardSelection(uniqueId) },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            }

                            if (movie != null) {
                                movie.voteAverage?.let {
                                    val screenWidthvote =
                                        LocalConfiguration.current.screenWidthDp.dp
                                    val votesize = if (screenWidthvote <= 800.dp) 14.sp else 24.sp
                                    Text(
                                        text = "Rating = $it",
                                        style = TextStyle(fontSize = votesize),
                                        maxLines = 1,
                                        color=Color(0xFF2C3E50),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                            if (movie != null) {
                                movie.overview?.let { overviewText ->
                                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                                    val overviewtext = if (screenWidth <= 800.dp) 14.sp
                                    else if (screenWidth>=800.dp && isMovieSelected) 20.sp
                                    else 24.sp

                                    Text(
                                        text = overviewText,
                                        style = TextStyle.Default.copy(fontSize = overviewtext, color = Color(0xFF2C3E50)),
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis


                                        )



                                }

                            }

                        }

                    }
                }


        }
    }

}




@Composable
fun MovieImage(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Movie Poster",
        modifier = Modifier
            .width((LocalDensity.current.density * LocalConfiguration.current.screenWidthDp * 0.1f).dp)
            .fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MovieImage2(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Movie Poster",
        modifier = Modifier
            .width((LocalDensity.current.density * LocalConfiguration.current.screenWidthDp * 0.2f).dp)
            .fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MovieImage3(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Movie Poster",
        modifier = Modifier
            .width((LocalDensity.current.density * LocalConfiguration.current.screenWidthDp * 0.5f).dp)
            .fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}
