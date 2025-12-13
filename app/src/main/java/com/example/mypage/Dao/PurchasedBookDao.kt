package com.example.mypage.Dao



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mypage.models.PurchasedBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedBookDao {

    @Query("SELECT * FROM purchased_books")
    fun getAllPurchased(): Flow<List<PurchasedBookEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchased(purchased: PurchasedBookEntity)

    @Query("DELETE FROM purchased_books")
    suspend fun clearPurchased()
}
