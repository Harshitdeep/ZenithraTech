package com.example.zenithra.mangaScreen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_table")
data class MangaEntity(
    @PrimaryKey val mal_id: Int,
    val title: String,
    val imageUrl: String,
    val synopsis: String?,
    val score: Float?
)