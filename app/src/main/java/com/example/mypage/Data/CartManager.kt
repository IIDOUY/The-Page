package com.example.mypage.Data

object CartManager {
    private val _cartItems = mutableListOf<Book>()
    val cartItems: List<Book> get() = _cartItems.toList()

    fun addToCart(book: Book) {

        if (!_cartItems.any { it.id == book.id }) {
            _cartItems.add(book)
        }
    }

    fun removeFromCart(bookId: Int) {
        _cartItems.removeAll { it.id == bookId }
    }

    fun getTotal(): Double {
        return _cartItems.sumOf { it.price }
    }

    fun clearCart() {
        _cartItems.clear()
    }
}