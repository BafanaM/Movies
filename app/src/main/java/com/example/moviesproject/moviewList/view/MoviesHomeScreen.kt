package com.example.moviesproject.moviewList.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviesproject.moviewList.util.Category
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun MoviesHomeScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            onEvent(MovieListUiEvent.Refresh)
        }
    ) {
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                delay(1000)
                isRefreshing = false
            }
        }

        if (movieListState.nowPlayingMovieList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(movieListState.nowPlayingMovieList.size) { index ->
                    MovieItem(
                        movie = movieListState.nowPlayingMovieList[index],
                        navHostController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                    )

                    if (index >= movieListState.nowPlayingMovieList.size - 1 && !movieListState.isLoading) {
                        onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                    }
                }
            }
        }
    }
}