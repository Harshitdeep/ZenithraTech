package com.example.zenithra

import android.app.Application
import androidx.room.Room
import com.example.zenithra.AppDatabase.Companion.MIGRATION_1_2

class App : Application() {

    lateinit var appDatabase: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this  // Set global instance

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
