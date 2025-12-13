package com.example.mypage.repository

// repository/CartRepository.kt


import com.example.mypage.Dao.CartItemDao
import com.example.mypage.models.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val dao: CartItemDao
) {
    val cartItems: Flow<List<CartItemEntity>> = dao.getAllCartItems()

    suspend fun addToCart(bookId: Int) {
        val existing = dao.findById(bookId)
        val newQuantity = (existing?.quantity ?: 0) + 1
        dao.upsert(CartItemEntity(bookId = bookId, quantity = newQuantity))
    }

    suspend fun removeFromCart(bookId: Int) {
        val existing = dao.findById(bookId) ?: return
        if (existing.quantity > 1) {
            dao.upsert(existing.copy(quantity = existing.quantity - 1))
        } else {
            dao.delete(existing)
        }
    }

    suspend fun clearCart() = dao.clearCart()
}
