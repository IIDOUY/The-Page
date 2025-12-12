// com/example/mypage/repository/BookRepository.kt
package com.example.mypage.repository

import com.example.mypage.models.BooksResponse
import com.example.mypage.network.GutendexApiService
import com.example.mypage.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(
    private val apiService: GutendexApiService
) {

    fun getAllBooks(page: Int = 1): Flow<Result<BooksResponse>> = flow {
        try {
            emit(Result.Loading)
            val response = withContext(Dispatchers.IO) {
                apiService.getAllBooks(page = page)
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun searchBooks(query: String): Flow<Result<BooksResponse>> = flow {
        try {
            emit(Result.Loading)
            val response = withContext(Dispatchers.IO) {
                apiService.getBooks(search = query)
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getBooksByTopic(topic: String): Flow<Result<BooksResponse>> = flow {
        try {
            emit(Result.Loading)
            val response = withContext(Dispatchers.IO) {
                apiService.getBooks(topic = topic)
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}
