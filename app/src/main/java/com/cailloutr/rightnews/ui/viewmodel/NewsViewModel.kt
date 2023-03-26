package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.model.BannerNews
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    private val _sectionsState = MutableStateFlow<List<Section>>(listOf())
    val sectionsState: StateFlow<List<Section>> = _sectionsState.asStateFlow()

    init {
        getSectionsFilteredById(
            listOf(
                "games",
                "sport",
                "tech",
                "books",
                "world-news",
                "politics",
                "culture",
                "education"
            )
        )
    }

    fun getSectionsFilteredById(sections: List<String>? = null) {
        viewModelScope.launch(dispatchers.main) {
            _sectionsState.value = newsUseCases.getSectionsFilteredByIdUseCase(sections)
        }
    }

    // Mocked up list
    val bannersList = MutableStateFlow<List<BannerNews>>(
        listOf(
            BannerNews(
                id = 1,
                title = "Teste",
                author = "John Doe",
                description = "Donec lobortis justo vitae feugiat iaculis. Sed eleifend hendrerit aliquam. Praesent lacinia odio at lobortis cursus. Proin gravida ipsum a nisi ullamcorper elementum. ",
                image = "https://images.unsplash.com/photo-1533450718592-29d45635f0a9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
            ),
            BannerNews(
                id = 2,
                title = "Teste",
                author = "John Doe",
                description = "Donec lobortis justo vitae feugiat iaculis. Sed eleifend hendrerit aliquam. Praesent lacinia odio at lobortis cursus. Proin gravida ipsum a nisi ullamcorper elementum. ",
                image = "https://images.unsplash.com/photo-1533450718592-29d45635f0a9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
            ),
            BannerNews(
                id = 3,
                title = "Teste",
                author = "John Doe",
                description = "Donec lobortis justo vitae feugiat iaculis. Sed eleifend hendrerit aliquam. Praesent lacinia odio at lobortis cursus. Proin gravida ipsum a nisi ullamcorper elementum. ",
                image = "https://images.unsplash.com/photo-1533450718592-29d45635f0a9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
            ),
            BannerNews(
                id = 4,
                title = "Teste",
                author = "John Doe",
                description = "Donec lobortis justo vitae feugiat iaculis. Sed eleifend hendrerit aliquam. Praesent lacinia odio at lobortis cursus. Proin gravida ipsum a nisi ullamcorper elementum. ",
                image = "https://images.unsplash.com/photo-1533450718592-29d45635f0a9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
            ),
        )
    ).asStateFlow()
}