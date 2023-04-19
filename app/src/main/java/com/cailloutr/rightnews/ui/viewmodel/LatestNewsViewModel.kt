package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestNewsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {


    private val _latestNewsState =
        MutableStateFlow<NewsContainer?>(null)
    val latestNewsState: StateFlow<NewsContainer?> = _latestNewsState.asStateFlow()

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


/*    val pagingDataFlow: Flow<PagingData<Article>> = getNews()
        .cachedIn(viewModelScope)*/

/*    val hasNotScrolledForCurrentSearch: Boolean = false*/

/*    private fun getNews(): Flow<PagingData<Article>> =
        newsUseCases.getRecentNewsUseCase()*/

    fun getLatestNews(orderBy: OrderBy, fields: String, page: Int) {
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value =
                newsUseCases.getNewsBySectionUseCase(
                    dispatchers.io,
                    SectionWrapper(LATEST_NEWS, "")
                ).first()
        }
    }

    fun getNewsOfSection(section: SectionWrapper) {
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value =
                newsUseCases.getNewsBySectionUseCase(
                    dispatchers.io,
                    section
                ).first()
        }
    }

    fun setShouldRefresh(value: Boolean) {
        _isRefreshing.value = value
    }

}