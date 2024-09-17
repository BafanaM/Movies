package com.example.moviesproject.moviewList.view

sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
    data object Navigate: MovieListUiEvent
    data object Refresh : MovieListUiEvent

}