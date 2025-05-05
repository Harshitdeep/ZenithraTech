package com.example.zenithra.mangaScreen

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mangaList: List<MangaEntity>)

    @Query("SELECT * FROM manga_table")
    suspend fun getAllManga(): List<MangaEntity>
}
