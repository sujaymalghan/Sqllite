package com.example.assignment6

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun MovieDetails(movieId: Int?, movieViewModel: MovieViewModel) {

    val movie by movieViewModel.getMovieById(movieId ?: -1).observeAsState()

    LaunchedEffect(movie) {
        movieViewModel.setCurrentMovie(movie)
    }

    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF76c7d0))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            val k = movie?.posterPath
            val imageUrl = "https://image.tmdb.org/t/p/w400/$k"
            MovieImage2(imageUrl = imageUrl)

            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                    append("Movie Title: ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF333333), fontWeight = FontWeight.Bold)) {
                    append(movie?.title ?: "N/A")
                }
            }
            Text(text = text, modifier = Modifier.padding(8.dp))

            val adjustedRating = (movie?.voteAverage?.toDouble() ?: 0.0) / 2
            RatingBar(currentRating = adjustedRating, modifier = Modifier.align(Alignment.CenterHorizontally))

            // Rating
            val ratingText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                    append("Rating: ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF333333), fontWeight = FontWeight.Bold)) {
                    append("${movie?.voteAverage}" ?: "N/A")
                }
            }
            Text(text = ratingText, modifier = Modifier.padding(8.dp))

            // Release Date
            val releaseDateText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                    append("Release Date: ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF333333), fontWeight = FontWeight.Bold)) {
                    append(movie?.releaseDate ?: "N/A")
                }
            }
            Text(text = releaseDateText, modifier = Modifier.padding(8.dp))

            val originalLanguageText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                    append("Original Language: ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF333333), fontWeight = FontWeight.Bold)) {
                    append(movie?.originalLanguage ?: "N/A")
                }
            }
            Text(text = originalLanguageText, modifier = Modifier.padding(8.dp))

            Text(
                text = movie?.overview ?: "N/A",
                color = Color(0xFF333333),
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }

}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    max: Int = 5,
    currentRating: Double,
    size: Dp = 24.dp
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..max) {
            when {
                currentRating >= i -> {

                    FullStar(size)
                }
                currentRating > i - 0.5 && currentRating < i -> {

                    HalfFilledStar(size)
                }
                else -> {

                    EmptyStar(size)
                }
            }
        }
    }
}


@Composable
fun FullStar(size: Dp) {
    Image(
        painter = painterResource(id = R.drawable.fullstar),
        contentDescription = "Full Star",
        modifier = Modifier.size(size),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun EmptyStar(size: Dp) {
    Image(
        painter = painterResource(id = R.drawable.emptystar),
        contentDescription = "Empty Star",
        modifier = Modifier.size(size),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun HalfFilledStar(size: Dp) {
    Image(
        painter = painterResource(id = R.drawable.halfstar),
        contentDescription = "Half Star",
        modifier = Modifier.size(size),
        contentScale = ContentScale.FillBounds
    )
}

