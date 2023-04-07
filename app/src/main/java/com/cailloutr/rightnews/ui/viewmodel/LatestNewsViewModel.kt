package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
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
class LatestNewsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    private val _latestNewsState =
        MutableStateFlow<Resource<NewsContainer>>(Resource.loading(data = null))
    val latestNewsState: StateFlow<Resource<NewsContainer>> = _latestNewsState.asStateFlow()


    fun getLatestNews(orderBy: OrderBy, fields: String) {
        _latestNewsState.value = Resource.loading(data = null)
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value = newsUseCases.getRecentNewsUseCase(orderBy, fields)
        }
    }

    fun getNewsOfSection(section: String) {
        _latestNewsState.value = Resource.loading(data = null)
        viewModelScope.launch {
            _latestNewsState.value = newsUseCases.getNewsBySectionUseCase(section)
        }
    }

}