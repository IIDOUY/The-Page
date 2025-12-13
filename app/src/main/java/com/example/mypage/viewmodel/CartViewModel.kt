package com.example.mypage.viewmodel

// viewmodel/CartViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypage.models.CartItemEntity
import com.example.mypage.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemEntity>> =
        repository.cartItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(bookId: Int) {
        viewModelScope.launch {
            repository.addToCart(bookId)
        }
    }

    fun removeFromCart(bookId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(bookId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}
