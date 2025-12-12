package com.example.mypage.Dao



import androidx.room.*
import com.example.mypage.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun emailExists(email: String): Boolean

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastUser(): UserEntity?
}