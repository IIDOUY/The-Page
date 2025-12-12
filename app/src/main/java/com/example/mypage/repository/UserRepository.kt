package com.example.mypage.repository


import com.example.mypage.Dao.UserDao
import com.example.mypage.models.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity): Result<Long> {
        return try {
            if (userDao.emailExists(user.email)) {
                Result.failure(Exception("Email already exists"))
            } else {
                val userId = userDao.insertUser(user)
                Result.success(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun emailExists(email: String): Boolean = userDao.emailExists(email)

    suspend fun getLastUser(): UserEntity? = userDao.getLastUser()
}