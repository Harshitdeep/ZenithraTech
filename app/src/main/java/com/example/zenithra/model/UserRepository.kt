package com.example.zenithra.model

import com.example.zenithra.AppDatabase
import com.example.zenithra.UI.User

class UserRepository(private val db: AppDatabase) {

    private val userDao = db.userDao()

    suspend fun isUserExist(email: String): Boolean {
        return userDao.isUserExist(email) > 0
    }

    suspend fun createUser(email: String, password: String) {
        val newUser = User(email = email, password = password)
        userDao.insertUser(newUser)
    }

    suspend fun checkUserCredentials(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user?.password == password
    }
}