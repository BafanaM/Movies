package com.example.moviesproject.moviewList.view

import com.example.moviesproject.moviewList.domain.model.Movie

data class DetailsSate(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
