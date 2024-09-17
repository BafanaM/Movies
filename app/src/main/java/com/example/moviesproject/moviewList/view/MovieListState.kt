package com.example.moviesproject.moviewList.view

import com.example.moviesproject.moviewList.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val nowPlayingListPage: Int = 1,
    val nowPlayingMovieList: List<Movie> = emptyList()
)
