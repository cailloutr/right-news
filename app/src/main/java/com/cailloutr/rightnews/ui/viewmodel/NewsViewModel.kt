package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cailloutr.rightnews.model.BannerNews
import com.cailloutr.rightnews.ui.chip.ChipItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : ViewModel() {

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

    val sectionsList = MutableStateFlow(
        listOf(
            ChipItem(id = 1, "Sport"),
            ChipItem(id = 2, "Tech"),
            ChipItem(id = 3, "Weather"),
            ChipItem(id = 4, "Politics"),
            ChipItem(id = 5, "Economics"),
            ChipItem(id = 6, "Economics"),
            ChipItem(id = 7, "Economics"),
        )
    ).asStateFlow()

}