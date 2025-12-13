package com.example.mypage.models

//CartItemEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val bookId: Int,
    val quantity: Int = 1
)
