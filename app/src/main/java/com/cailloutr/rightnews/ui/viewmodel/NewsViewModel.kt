package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.NewsResponse
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.other.Resource
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

    private val _latestNewsState =
        MutableStateFlow<Resource<NewsResponse>>(Resource.loading(data = null))
    val latestNewsState: StateFlow<Resource<NewsResponse>?> = _latestNewsState.asStateFlow()

    init {
        getSectionsFilteredById(Constants.DEFAULT_SECTIONS)
        getLatestNews(OrderBy.NEWEST, Constants.API_CALL_FIELDS)
    }


    fun getSectionsFilteredById(sections: List<String>? = null) {
        viewModelScope.launch(dispatchers.main) {
            _sectionsState.value = newsUseCases.getSectionsFilteredByIdUseCase(sections)
        }
    }

    //TODO: Test
    fun getLatestNews(orderBy: OrderBy, fields: String) {
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value = newsUseCases.getRecentNewsUseCase(
                orderBy,
                fields
            )
        }
    }
}