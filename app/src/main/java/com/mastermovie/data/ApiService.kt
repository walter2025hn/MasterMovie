package com.mastermovie.data

import retrofit2.http.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient

data class LoginRequest(val user: String, val pass: String)
data class LoginResponse(val token: String, val expires: String?) // expires optional
data class Stream(val url: String, val quality: String) // quality: low,medium,high
data class MediaItem(val id: String, val title: String, val poster: String, val streams: List<Stream>)

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("/api/movies")
    suspend fun movies(@Header("Authorization") token: String, @Query("page") page:Int, @Query("size") size:Int): Response<List<MediaItem>>

    @GET("/api/series")
    suspend fun series(@Header("Authorization") token: String, @Query("page") page:Int, @Query("size") size:Int): Response<List<MediaItem>>
}

object ApiClient {
    fun create(tokenProvider: (() -> String?)? = null): ApiService {
        val clientBuilder = OkHttpClient.Builder()

        if (tokenProvider != null) {
            clientBuilder.addInterceptor { chain ->
                val reqBuilder = chain.request().newBuilder()
                tokenProvider()?.let { reqBuilder.addHeader("Authorization", "Bearer $it") }
                chain.proceed(reqBuilder.build())
            }
        }

        val base = com.mastermovie.BuildConfig.SERVER_URL.ifEmpty { "https://your-server.example" }

        val retrofit = Retrofit.Builder()
            .baseUrl(base)
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
