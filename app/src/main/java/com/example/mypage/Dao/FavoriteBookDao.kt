package com.example.mypage.Dao

// Dao/FavoriteBookDao.kt


import androidx.room.*
import com.example.mypage.models.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {

    @Query("SELECT * FROM favorite_books")
    fun getAllFavorites(): Flow<List<FavoriteBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteBookEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteBookEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE bookId = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
