package com.example.moviesproject.moviewList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.moviesproject.R
import com.example.moviesproject.moviewList.viewModel.MovieListVieModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListVieModel>()
    val movieState = movieListViewModel.movieListState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.movies), fontSize = 16.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseSurface
                )
            )
        }
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){

        }

    }
}