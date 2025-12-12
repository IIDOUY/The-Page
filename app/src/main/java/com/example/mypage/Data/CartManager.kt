package com.example.mypage.Data

import com.example.mypage.models.Book

object CartManager {
    private val _cartItems = mutableListOf<Book>()
    private val _purchasedBooks = mutableListOf<Book>()

    val cartItems: List<Book>
        get() = _cartItems.toList()

    val purchasedBooks: List<Book>
        get() = _purchasedBooks.toList()

    fun addToCart(book: Book) {
        if (!_cartItems.any { it.id == book.id }) {
            _cartItems.add(book)
        }
    }

    fun removeFromCart(bookId: Int) {
        _cartItems.removeAll { it.id == bookId }
    }

    fun addToPurchased(book: Book) {
        if (!_purchasedBooks.any { it.id == book.id }) {
            _purchasedBooks.add(book)
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun clearPurchased() {
        _purchasedBooks.clear()
    }
}