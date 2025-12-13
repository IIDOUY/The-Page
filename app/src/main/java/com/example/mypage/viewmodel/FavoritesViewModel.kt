package com.example.mypage.viewmodel

// viewmodel/FavoritesViewModel.kt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypage.repository.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    val favoriteIds: StateFlow<Set<Int>> =
        repository.favoritesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun onFavoriteClick(bookId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(bookId)
        }
    }
}
