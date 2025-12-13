package com.example.mypage.repository

// repository/FavoritesRepository.kt


import com.example.mypage.Dao.FavoriteBookDao
import com.example.mypage.models.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoritesRepository(
    private val dao: FavoriteBookDao
) {
    val favoritesFlow: Flow<Set<Int>> =
        dao.getAllFavorites().map { list -> list.map { it.bookId }.toSet() }

    suspend fun toggleFavorite(bookId: Int) {
        val isFav = dao.isFavorite(bookId)
        if (isFav) {
            dao.deleteFavorite(FavoriteBookEntity(bookId))
        } else {
            dao.insertFavorite(FavoriteBookEntity(bookId))
        }
    }
}
