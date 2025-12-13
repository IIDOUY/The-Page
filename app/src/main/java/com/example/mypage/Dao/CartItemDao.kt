package com.example.mypage.Dao

// Dao/CartItemDao.kt

import androidx.room.*
import com.example.mypage.models.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cartItem: CartItemEntity)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE bookId = :id LIMIT 1")
    suspend fun findById(id: Int): CartItemEntity?
}
