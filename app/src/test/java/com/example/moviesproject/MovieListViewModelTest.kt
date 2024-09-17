package com.example.moviesproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.moviesproject.moviewList.domain.model.Movie
import com.example.moviesproject.moviewList.domain.repository.MovieListRepository
import com.example.moviesproject.moviewList.util.Resource
import com.example.moviesproject.moviewList.viewModel.MovieListVieModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@FlowPreview
class MovieListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Mock
    private lateinit var movieListRepository: MovieListRepository

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: MovieListVieModel

    private val mockMovieList = listOf(
        Movie(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            release_date = "2010-07-16",
            vote_average = 8.8,
            poster_path = "/inception.jpg",
            genre_ids = listOf(1, 2),
            popularity = 100.0,
            video = false,
            adult = false,
            backdrop_path = "/path/to/backdrop.jpg",
            original_language = "en",
            original_title = "Inception",
            vote_count = 100,
            category = "Popular"
        ),
        Movie(
            id = 2,
            title = "Interstellar",
            overview = "A journey beyond the stars",
            release_date = "2014-11-07",
            vote_average = 8.6,
            poster_path = "/interstellar.jpg",
            genre_ids = listOf(1, 2),
            popularity = 120.0,
            video = false,
            adult = false,
            backdrop_path = "/path/to/backdrop.jpg",
            original_language = "en",
            original_title = "Interstellar",
            vote_count = 200,
            category = "Popular"
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        savedStateHandle = SavedStateHandle(mapOf("category" to "Popular"))
        viewModel = MovieListVieModel(movieListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test initial state is loading`() = testScope.runTest {
        val flow = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading(true))

        `when`(movieListRepository.getMovieList(
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
        )).thenReturn(flow)

        val job: Job = launch {
            viewModel.movieListState.collectLatest { state ->
                assertTrue(state.isLoading)
                assertEquals(emptyList<Movie>(), state.isLoading)
            }
        }

        job.cancel()
    }

    @Test
    fun `test getMovieList updates StateFlow with success`() = testScope.runTest {
        val flow = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading(true))

        `when`(movieListRepository.getMovieList(
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
        )).thenReturn(flow)

        val job: Job = launch {
            viewModel.movieListState.collectLatest { state ->
                if (!state.isLoading) {
                    assertEquals(mockMovieList, state.isLoading)
                }
            }
        }

        flow.value = Resource.Success(mockMovieList)

        job.cancel()
    }

    @Test
    fun `test getMovieList updates StateFlow with error`() = testScope.runTest {
        val expectedErrorMessage = "Error fetching movies"

        val flow = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading(true))

        `when`(movieListRepository.getMovieList(
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
        )).thenReturn(flow)

        val job: Job = launch {
            viewModel.movieListState.collectLatest { state ->
                if (!state.isLoading) {
                    assertEquals(emptyList<Movie>(), state.isLoading)
                    assertEquals(expectedErrorMessage, state.isLoading)
                }
            }
        }

        flow.value = Resource.Error(expectedErrorMessage, null)

        job.cancel()
    }
}