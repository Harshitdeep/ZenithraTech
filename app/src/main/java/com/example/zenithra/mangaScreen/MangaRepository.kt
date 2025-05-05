package com.example.zenithra.mangaScreen

import com.example.zenithra.model.JikanApiService

class MangaRepository(
    private val api: JikanApiService,
    private val dao: MangaDao
) {

    // Fetch a list of top mangas for the given page
    suspend fun fetchManga(page: Int): List<MangaEntity> {
        return try {
            val response = api.getTopManga(page)
            val mangaEntities = response.data.map {
                MangaEntity(
                    mal_id = it.mal_id,
                    title = it.title,
                    imageUrl = it.images.jpg.image_url,
                    synopsis = it.synopsis
                        ?: "No description available",  // Default value for null synopsis
                    score = it.score
                )
            }
            dao.insertAll(mangaEntities)
            mangaEntities
        } catch (e: Exception) {
            val localCache = dao.getAllManga()
            if (localCache.isNotEmpty()) {
                localCache // Return local cache if available
            } else {
                throw e // Rethrow if no local cache is available
            }
        }
    }

    suspend fun getMangaDetails(id: Int): MangaDetail {
        return try {
            println("Fetching manga details for ID: $id")

            val response = api.getMangaDetails(id.toString())  // Now returns MangaDetailResponse
            val manga = response.data

            MangaDetail(
                mal_id = manga.mal_id,
                title = manga.title,
                title_english = null, // Optional, update if needed
                imageUrl = manga.images.jpg.image_url,
                synopsis = manga.synopsis ?: "No description available.",
                score = manga.score
            )
        } catch (e: Exception) {
            println("Error fetching manga details: ${e.message}")
            throw e
        }
    }

}
