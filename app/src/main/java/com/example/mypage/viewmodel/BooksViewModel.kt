package com.example.mypage.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypage.models.Book
import com.example.mypage.models.BooksResponse
import com.example.mypage.repository.BookRepository

import com.example.mypage.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BooksUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val totalResults: Int = 0,
    val hasNextPage: Boolean = false
)

class BooksViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        loadAllBooks()
    }

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            loadAllBooks()
            return
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            error = null
        )

        viewModelScope.launch {
            repository.searchBooks(query).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        val response: BooksResponse = result.data
                        _uiState.value = _uiState.value.copy(
                            books = response.results,
                            isLoading = false,
                            error = null,
                            totalResults = response.count,
                            hasNextPage = response.next != null,
                            currentPage = 1
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception
                        )
                    }
                }
            }
        }
    }

    fun loadAllBooks() {
        viewModelScope.launch {
            repository.getAllBooks(page = 1).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        val response: BooksResponse = result.data
                        _uiState.value = _uiState.value.copy(
                            books = response.results,
                            isLoading = false,
                            error = null,
                            totalResults = response.count,
                            hasNextPage = response.next != null,
                            currentPage = 1
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception
                        )
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (!state.hasNextPage || state.isLoading) return

        val nextPage = state.currentPage + 1

        viewModelScope.launch {
            repository.getAllBooks(page = nextPage).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        val response: BooksResponse = result.data
                        val currentBooks = _uiState.value.books
                        _uiState.value = _uiState.value.copy(
                            books = currentBooks + response.results,
                            isLoading = false,
                            error = null,
                            totalResults = response.count,
                            hasNextPage = response.next != null,
                            currentPage = nextPage
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception
                        )
                    }
                }
            }
        }
    }

    fun getBooksByTopic(topic: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = topic,
            error = null
        )

        viewModelScope.launch {
            repository.getBooksByTopic(topic).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        val response: BooksResponse = result.data
                        _uiState.value = _uiState.value.copy(
                            books = response.results,
                            isLoading = false,
                            error = null,
                            totalResults = response.count,
                            hasNextPage = response.next != null,
                            currentPage = 1
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception
                        )
                    }
                }
            }
        }
    }
}
