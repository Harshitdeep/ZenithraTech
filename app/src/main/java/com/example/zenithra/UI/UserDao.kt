package com.example.zenithra.UI

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    // Insert a new user
    @Insert
    suspend fun insertUser(user: User)

    // Check if a user with the given email exists
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun isUserExist(email: String): Int

    // Get user by email
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}


