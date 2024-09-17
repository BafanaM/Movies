package com.example.moviesproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesproject.moviewList.util.Screen
import com.example.moviesproject.moviewList.view.HomeScreen
import com.example.moviesproject.moviewList.viewModel.MovieListVieModel
import com.example.moviesproject.ui.theme.MoviesProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel = hiltViewModel<MovieListVieModel>()

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Home.rout
            ) {
                composable(Screen.Home.rout) {
                    HomeScreen(navController)
                }

                composable(Screen.Details.rout + "/{movieId}",
                    arguments = listOf(
                        navArgument("movieId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    DetailsScreen(backStackEntry)
                }
            }
        }
    }
}