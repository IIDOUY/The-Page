package com.example.mypage.models



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchased_books")
data class PurchasedBookEntity(
    @PrimaryKey val bookId: Int
)
