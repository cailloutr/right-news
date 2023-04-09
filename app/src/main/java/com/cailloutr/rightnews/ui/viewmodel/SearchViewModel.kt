package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SearchViewModel @Inject constructor(
    private val dispatcher: DispatchersProvider,
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _searchResultState =
        MutableStateFlow<Resource<NewsContainer>?>(null)
    val searchResultState: StateFlow<Resource<NewsContainer>?> = _searchResultState.asStateFlow()

    fun getSearchResult(query: String) {
        _searchResultState.value = Resource.loading(data = null)
        viewModelScope.launch(dispatcher.main) {
            _searchResultState.value = newsUseCases.searchNewsUseCase(query)
        }
    }
}