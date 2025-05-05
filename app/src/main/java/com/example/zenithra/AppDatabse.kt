package com.example.zenithra

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zenithra.UI.User
import com.example.zenithra.UI.UserDao
import com.example.zenithra.mangaScreen.MangaDao
import com.example.zenithra.mangaScreen.MangaEntity

@Database(entities = [User::class, MangaEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS manga_table (
                        mal_id INTEGER PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        synopsis TEXT,
                        score REAL
                    )
                """.trimIndent()
                )
            }
        }
    }
}


