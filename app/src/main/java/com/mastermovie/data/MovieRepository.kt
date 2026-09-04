package com.mastermovie.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val api: ApiService) {
    private val pageSize = 100

    suspend fun loadMoviesPaged(token: String, page:Int): List<MediaItem> = withContext(Dispatchers.IO) {
        val resp = api.movies("Bearer $token", page, pageSize)
        if (resp.isSuccessful) resp.body() ?: emptyList() else emptyList()
    }

    suspend fun loadSeriesPaged(token:String, page:Int): List<MediaItem> = withContext(Dispatchers.IO) {
        val resp = api.series("Bearer $token", page, pageSize)
        if (resp.isSuccessful) resp.body() ?: emptyList() else emptyList()
    }
}
