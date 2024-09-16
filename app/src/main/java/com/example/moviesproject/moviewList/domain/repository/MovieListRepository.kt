package com.example.moviesproject.moviewList.domain.repository

import com.example.moviesproject.moviewList.domain.model.Movie
import com.example.moviesproject.moviewList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {

    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}