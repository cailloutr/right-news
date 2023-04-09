package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import com.cailloutr.rightnews.data.NewsDataSource
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.News
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    val pager = Pager(
        config = NewsDataSource.pagingConfig,
        pagingSourceFactory = { NewsDataSource() }
    )

    val _pagerState: Flow<PagingData<News>> = pager.flow

    fun getLatestNews(orderBy: OrderBy, fields: String, page: Int) {
        _latestNewsState.value = Resource.loading(data = null)
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value = newsUseCases.getRecentNewsUseCase(orderBy, fields, page)
        }
    }

    fun getNewsOfSection(section: String) {
        _latestNewsState.value = Resource.loading(data = null)
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value = newsUseCases.getNewsBySectionUseCase(section)
        }
    }

}