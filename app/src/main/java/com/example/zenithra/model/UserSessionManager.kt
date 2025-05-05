package com.example.zenithra.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object UserSessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val PREF = "MangaLikePreferences"
    private const val KEY_LIKE_STATE = "like_state"

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLoginState(context: Context, isLoggedIn: Boolean, email: String? = null) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        email?.let {
            editor.putString(KEY_USER_EMAIL, it)
        }
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun clearSession(context: Context) {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
    Log.d("Session", "Session cleared!")
    }

    // Save the like state
    fun saveLikeState(context: Context, mangaId: Int, isLiked: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("$KEY_LIKE_STATE$mangaId", isLiked).apply()
    }

    // Get the like state
    fun getLikeState(context: Context, mangaId: Int): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("$KEY_LIKE_STATE$mangaId", false)  // Default to false
    }
}