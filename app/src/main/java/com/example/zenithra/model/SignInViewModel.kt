package com.example.zenithra.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithra.App
import com.example.zenithra.UI.User
import com.example.zenithra.UI.UserDao
import kotlinx.coroutines.launch

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = (application as App).appDatabase.userDao()

    // Function to handle both Sign In and Sign Up
    fun signInOrCreateUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // First, check if the user exists in the database
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser != null) {
                    // If the user exists, validate the password
                    if (existingUser.password == password) {
                        // Successful login
                        UserSessionManager.saveLoginState(getApplication(), true)
                        onSuccess()
                    } else {
                        // Password mismatch, return error
                        onError("Invalid password")
                    }
                } else {
                    // If the user does not exist, create a new user
                    val hashedPassword = hashPassword(password)
                    userDao.insertUser(User(email = email, password = hashedPassword))
                    UserSessionManager.saveLoginState(getApplication(), true)
                    onSuccess()  // Successfully created the account and logged in
                }
            } catch (e: Exception) {
                // Handle unexpected errors (e.g., database issues)
                onError(e.message ?: "Unknown error")
            }
        }
    }

    // Dummy password hashing function, replace with real hashing
    private fun hashPassword(password: String): String {
        // Ideally, you'd use proper hashing like SHA-256, bcrypt, etc.
        return password
    }
}