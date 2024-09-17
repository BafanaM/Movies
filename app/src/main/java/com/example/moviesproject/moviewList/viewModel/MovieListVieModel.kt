package com.example.moviesproject.moviewList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.moviewList.domain.repository.MovieListRepository
import com.example.moviesproject.moviewList.util.Category
import com.example.moviesproject.moviewList.util.Resource
import com.example.moviesproject.moviewList.view.MovieListState
import com.example.moviesproject.moviewList.view.MovieListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListVieModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getNowPlayingMovies(false)
    }

    fun onEvent(event: MovieListUiEvent) {
        when(event) {
            MovieListUiEvent.Navigate -> {
                // Handle navigation if needed
            }
            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.NOWPLAYING) {
                    getNowPlayingMovies(true)
                }
            }
            MovieListUiEvent.Refresh -> {
                refreshMovies()
            }
        }
    }

    private fun getNowPlayingMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.nowPlayingListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { nowPlayingList ->
                            _movieListState.update {
                                it.copy(
                                    nowPlayingMovieList = movieListState.value.nowPlayingMovieList
                                            + nowPlayingList,
                                    nowPlayingListPage = movieListState.value.nowPlayingListPage + 1,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote = true,
                category = Category.POPULAR,
                page = 1
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { nowPlayingList ->
                            _movieListState.update {
                                it.copy(
                                    nowPlayingMovieList = nowPlayingList.shuffled(),
                                    nowPlayingListPage = 2,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
}