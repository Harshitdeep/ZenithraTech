package com.example.zenithra.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithra.mangaScreen.MangaEntity
import com.example.zenithra.mangaScreen.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MangaViewModel(private val repository: MangaRepository) : ViewModel() {

    // State for holding the list of manga
    private val _mangaList = MutableStateFlow<List<MangaEntity>>(emptyList())
    val mangaList: StateFlow<List<MangaEntity>> = _mangaList

    // State to track loading status
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Function to load manga data
    fun loadManga(page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true  // Set loading state to true before data fetch
            try {
                val manga = repository.fetchManga(page)
                _mangaList.value = manga  // Update manga list once data is fetched
            } catch (e: Exception) {
                // Handle any errors (Optional: You can set an error state here)
            } finally {
                _isLoading.value = false  // Set loading state to false after data fetch
            }
        }
    }
}