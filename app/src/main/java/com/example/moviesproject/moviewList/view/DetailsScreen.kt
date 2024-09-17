package com.example.moviesproject.moviewList.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviesproject.R
import com.example.moviesproject.moviewList.data.remote.MovieApi
import com.example.moviesproject.moviewList.util.RatingBar
import com.example.moviesproject.moviewList.viewModel.DetailsViewModel

@Composable
fun DetailsScreen() {

    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsSate.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(modifier = androidx.compose.ui.Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {

        if (backDropImageState is AsyncImagePainter.State.Error) {

            Box(modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center) {
                Icon(
                    modifier = androidx.compose.ui.Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription =  detailsState.movie?.title
                )
            }
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {

            Image(modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = detailsState.movie?.title,
                contentScale = ContentScale.Crop)
        }

        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Box(modifier = androidx.compose.ui.Modifier
                .width(160.dp)
                .height(240.dp)) {

                if (posterImageState is AsyncImagePainter.State.Error) {

                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = androidx.compose.ui.Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {

                    Image(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            detailsState.movie?.let {movie ->
                Column(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = androidx.compose.ui.Modifier
                            .padding(start = 16.dp),
                        text = movie.title, fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier
                        .height(16.dp))

                    Row(modifier = androidx.compose.ui.Modifier
                        .padding(start = 16.dp)) {
                        RatingBar(
                            starsModifier = androidx.compose.ui.Modifier.size(18.dp),
                            rating = movie.vote_average/2
                        )

                        Text(modifier = androidx.compose.ui.Modifier
                            .padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))

                    Text(
                        modifier = androidx.compose.ui.Modifier
                            .padding(start = 16.dp),
                        text = stringResource(R.string.language) + movie.original_language
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(10.dp))

                    Text(
                        modifier = androidx.compose.ui.Modifier
                            .padding(start = 16.dp),
                        text = stringResource(R.string.release_date) + movie.release_date
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(10.dp))

                    Text(
                        modifier = androidx.compose.ui.Modifier
                            .padding(start = 16.dp),
                        text = stringResource(R.string.votes) + movie.vote_count
                    )

                }
            }
        }

        Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))

        Text(
            modifier = androidx.compose.ui.Modifier
                .padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

        detailsState.movie?.let {
            Text(
                modifier = androidx.compose.ui.Modifier
                    .padding(start = 16.dp),
                text = it.overview,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))
    }
}