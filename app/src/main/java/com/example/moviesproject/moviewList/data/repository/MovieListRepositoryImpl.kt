package com.example.moviesproject.moviewList.data.repository

import coil.network.HttpException
import com.example.moviesproject.moviewList.data.local.movie.MovieDatabase
import com.example.moviesproject.moviewList.data.mappers.toMovie
import com.example.moviesproject.moviewList.data.mappers.toMovieEntity
import com.example.moviesproject.moviewList.data.remote.MovieApi
import com.example.moviesproject.moviewList.domain.model.Movie
import com.example.moviesproject.moviewList.domain.repository.MovieListRepository
import com.example.moviesproject.moviewList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
)
    : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocal = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocal) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow

            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                emit(Resource.Error(message = "Movie loading error"))
                return@flow
            }
            catch (e: HttpException) {
                emit(Resource.Error(message = "Movie loading error"))
                return@flow

            }
            catch (e: Exception) {
                emit(Resource.Error(message = "Movie loading error"))
                return@flow

            }

            val movieEntities = movieListFromApi.results.let { 
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map {
                    it.toMovie(category)
                }
            ))

            emit(Resource.Loading(false))

        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity.toMovie(movieEntity.category))
                )

                emit(Resource.Loading(false))
                return@flow

            }

            emit(Resource.Error("Movie not found"))
            emit(Resource.Loading(false))

        }
    }
}