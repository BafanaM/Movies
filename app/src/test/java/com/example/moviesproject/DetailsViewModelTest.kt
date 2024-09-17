package com.example.moviesproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.moviesproject.moviewList.domain.model.Movie
import com.example.moviesproject.moviewList.domain.repository.MovieListRepository
import com.example.moviesproject.moviewList.util.Resource
import com.example.moviesproject.moviewList.viewModel.DetailsViewModel
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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@FlowPreview
class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Mock
    private lateinit var movieListRepository: MovieListRepository

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        savedStateHandle = SavedStateHandle(mapOf("movieId" to 1))
        viewModel = DetailsViewModel(movieListRepository, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test initial state is loading`() = testScope.runTest {
        val flow = MutableStateFlow<Resource<Movie>>(Resource.Loading(true))
        `when`(movieListRepository.getMovie(1)).thenReturn(flow)

        val job: Job = launch {
            viewModel.detailsSate.collectLatest { state ->
                assertTrue(state.isLoading)
                assertEquals(null, state.movie)
            }
        }

        job.cancel()
    }

    @Test
    fun `test getMovie updates StateFlow with success`() = testScope.runTest {
        val movieId = 1
        val expectedMovie = Movie(
            adult = false,
            backdrop_path = "/path/to/backdrop.jpg",
            genre_ids = listOf(1, 2, 3),
            original_language = "en",
            original_title = "Original Title",
            overview = "Movie Overview",
            popularity = 123.4,
            poster_path = "/path/to/poster.jpg",
            release_date = "2024-01-01",
            title = "Movie Title",
            video = false,
            vote_average = 7.8,
            vote_count = 100,
            id = movieId,
            category = "Action"
        )

        val flow = MutableStateFlow<Resource<Movie>>(Resource.Loading(true))
        `when`(movieListRepository.getMovie(movieId)).thenReturn(flow)

        val job: Job = launch {
            viewModel.detailsSate.collectLatest { state ->
                if (!state.isLoading) {
                    assertEquals(expectedMovie, state.movie)
                }
            }
        }

        flow.value = Resource.Success(expectedMovie)

        job.cancel()
    }

    @Test
    fun `test getMovie updates StateFlow with error`() = testScope.runTest {
        val movieId = 1
        val expectedErrorMessage = "Error fetching movie"

        val flow = MutableStateFlow<Resource<Movie>>(Resource.Loading(true))
        `when`(movieListRepository.getMovie(movieId)).thenReturn(flow)

        val job: Job = launch {
            viewModel.detailsSate.collectLatest { state ->
                if (!state.isLoading) {
                    assertEquals(null, state.movie)
                }
            }
        }

        flow.value = Resource.Error(expectedErrorMessage, null)

        job.cancel()
    }
}