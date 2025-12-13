package com.example.mypage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mypage.Dao.CartItemDao
import com.example.mypage.Dao.UserDao
import com.example.mypage.models.UserEntity
import com.example.mypage.Dao.FavoriteBookDao
import com.example.mypage.Dao.PurchasedBookDao
import com.example.mypage.models.CartItemEntity
import com.example.mypage.models.FavoriteBookEntity
import com.example.mypage.models.PurchasedBookEntity

@Database(
    entities = [UserEntity::class,
                FavoriteBookEntity::class ,
                CartItemEntity::class,
               PurchasedBookEntity ::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun favoriteBookDao(): FavoriteBookDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun purchasedBookDao(): PurchasedBookDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "the_page_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}