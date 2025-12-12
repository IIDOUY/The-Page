package com.example.mypage.network

import com.example.mypage.models.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


interface GutendexApiService {

    @GET("books")
    suspend fun getBooks(
        @Query("search") search: String? = null,
        @Query("topic") topic: String? = null,
        @Query("languages") languages: String? = null,
        @Query("page") page: Int = 1
    ): BooksResponse

    @GET("books")
    suspend fun getAllBooks(
        @Query("page") page: Int = 1
    ): BooksResponse

    companion object {
        private const val BASE_URL = "https://gutendex.com/"

        fun create(): GutendexApiService {
            // Setup OkHttpClient with logging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            // Create Retrofit instance
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GutendexApiService::class.java)
        }
    }
}