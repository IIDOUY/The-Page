package com.example.mypage.repository



import com.example.mypage.Dao.PurchasedBookDao
import com.example.mypage.models.PurchasedBookEntity
import kotlinx.coroutines.flow.Flow

class PurchasedRepository(
    private val dao: PurchasedBookDao
) {
    val purchasedFlow: Flow<List<PurchasedBookEntity>> = dao.getAllPurchased()

    suspend fun addPurchased(bookId: Int) {
        dao.insertPurchased(PurchasedBookEntity(bookId))
    }

    suspend fun clearPurchased() {
        dao.clearPurchased()
    }
}
