package com.example.zenithra.mangaScreen

data class MangaResponse(
    val data: List<MangaApiModel>
)

data class MangaApiModel(
    val mal_id: Int,
    val title: String,
    val images: Images,
    val synopsis: String?,
    val score: Float
)

data class Images(
    val jpg: ImageUrls
)

data class ImageUrls(
    val image_url: String
)

data class MangaDetail(
    val mal_id: Int,
    val title: String,
    val title_english: String?,
    val imageUrl: String,
    val synopsis: String,
    val score: Float  // Changed to Float for consistency
)

data class MangaDetailResponse(
    val data: MangaApiModel
)



