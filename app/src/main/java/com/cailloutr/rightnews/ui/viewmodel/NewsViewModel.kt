package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.FIRST_SECTIONS_ID
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.Sections
import com.cailloutr.rightnews.model.filter
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

    private val _sectionsListState = MutableStateFlow<Resource<Sections>>(Resource.loading(null))
    val sectionsListState: StateFlow<Resource<Sections>> = _sectionsListState.asStateFlow()

    private val _latestNewsState =
        MutableStateFlow<Resource<NewsContainer>>(Resource.loading(data = null))
    val latestNewsState: StateFlow<Resource<NewsContainer>> = _latestNewsState.asStateFlow()

    private val _sectionNewsState =
        MutableStateFlow<Resource<NewsContainer>>(Resource.loading(data = null))
    val sectionsNewsState: StateFlow<Resource<NewsContainer>> = _sectionNewsState.asStateFlow()

    private val _selectedSectionState = MutableStateFlow(FIRST_SECTIONS_ID)
    val selectedSectionsState: StateFlow<String> = _selectedSectionState.asStateFlow()

//    private lateinit var coroutineJob: Job

    init {
        fetchDataFromApi()
    }

    fun fetchDataFromApi() {
        _sectionsListState.value = Resource.loading(data = null)
        _latestNewsState.value = Resource.loading(data = null)
        _sectionNewsState.value = Resource.loading(data = null)

        getSectionsFilteredById(Constants.DEFAULT_SECTIONS)
        getLatestNews(OrderBy.NEWEST, Constants.API_CALL_FIELDS)
        getNewsBySection()
    }

    fun setSelectedSections(section: String) {
        _selectedSectionState.value = section
    }

    fun getSectionsFilteredById(sections: List<String>? = null) {
        viewModelScope.launch(dispatchers.main) {
            val responseData = newsUseCases.getSectionsUseCase().data
            _sectionsListState.value = newsUseCases.getSectionsUseCase().copy(
                data = responseData?.filter(sections)
            )
        }
    }

    fun getLatestNews(orderBy: OrderBy, fields: String) {
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value = newsUseCases.getRecentNewsUseCase(
                orderBy,
                fields
            )
        }
    }

    fun getNewsBySection() {
        viewModelScope.launch(dispatchers.main) {
            _sectionNewsState.value =
                newsUseCases.getNewsBySectionUseCase(selectedSectionsState.value)
        }
    }
}