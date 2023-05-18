package com.gabrielbmoro.programmingchallenge.ui.screens.details

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gabrielbmoro.programmingchallenge.R
import com.gabrielbmoro.programmingchallenge.ui.common.widgets.MovieDetailDescription
import com.gabrielbmoro.programmingchallenge.ui.common.widgets.MovieImage
import com.gabrielbmoro.programmingchallenge.domain.model.Movie
import com.gabrielbmoro.programmingchallenge.ui.common.theme.ThemePreviews
import com.gabrielbmoro.programmingchallenge.ui.common.widgets.AppToolbar
import com.gabrielbmoro.programmingchallenge.ui.common.widgets.MovieDetailIndicator

@Composable
private fun DetailsScreenMain(
    atTop: Boolean,
    uiState: DetailsUIState,
    scrollState: ScrollState,
    onFavoriteMovie: ((Boolean) -> Unit),
    onBackEvent: (() -> Unit)
) {
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = atTop,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AppToolbar(
                    title = uiState.movieTitle,
                    backEvent = onBackEvent
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = it.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MovieImage(
                imageUrl = uiState.imageUrl,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
            )

            MovieDetailIndicator(
                isFavorite = uiState.isFavorite,
                votesAverage = uiState.movieVotesAverage,
                onFavoriteMovie = onFavoriteMovie
            )

            Divider()

            MovieDetailDescription(
                titleRes = R.string.overview,
                content = {
                    Text(
                        text = uiState.movieOverview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            MovieDetailDescription(
                titleRes = R.string.popularity,
                content = {
                    Text(
                        text = uiState.moviePopularity.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            )

            MovieDetailDescription(
                titleRes = R.string.language,
                content = {
                    Text(
                        text = uiState.movieLanguage,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            )

            Spacer(modifier = Modifier.height(240.dp))
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    navController: NavController,
    movie: Movie,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState = remember { viewModel.uiState }

    val atTop = scrollState.value == 0

    DetailsScreenMain(
        atTop = atTop,
        uiState = uiState.value,
        scrollState = scrollState,
        onFavoriteMovie = {
            viewModel.isToFavoriteOrUnFavorite(it, movie)
        },
        onBackEvent = {
            navController.navigateUp()
        }
    )

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.setup(movie)
        }
    )
}

@ThemePreviews
@Composable
fun DetailsScreenPreview() {
    DetailsScreenMain(
        atTop = true,
        uiState = DetailsUIState.mocked1(),
        scrollState = ScrollState(0),
        onFavoriteMovie = {},
        onBackEvent = {},
    )
}