package com.example.mypage.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypage.repository.PurchasedRepository
import com.example.mypage.models.PurchasedBookEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PurchasedViewModel(
    private val repository: PurchasedRepository
) : ViewModel() {

    val purchasedBooks: StateFlow<List<PurchasedBookEntity>> =
        repository.purchasedFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPurchased(bookId: Int) {
        viewModelScope.launch {
            repository.addPurchased(bookId)
        }
    }

    fun clearPurchased() {
        viewModelScope.launch {
            repository.clearPurchased()
        }
    }
}
