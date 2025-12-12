package com.example.mypage.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mypage.repository.BookRepository

class BooksViewModelFactory(
    private val repository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BooksViewModel(repository) as T
    }
}