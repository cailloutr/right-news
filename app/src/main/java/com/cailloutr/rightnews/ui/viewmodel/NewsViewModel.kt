package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.constants.Constants.DEFAULT_SECTIONS
import com.cailloutr.rightnews.constants.Constants.FIRST_SECTIONS_ID
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.ui.Event
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    private val _sectionsListState = MutableStateFlow<List<RoomSection>>(listOf())
    val sectionsListState: StateFlow<List<RoomSection>> = _sectionsListState.asStateFlow()

    private val _latestNewsState =
        MutableStateFlow<NewsContainer?>(null)
    val latestNewsState: StateFlow<NewsContainer?> = _latestNewsState.asStateFlow()

    private val _sectionNewsState =
        MutableStateFlow<NewsContainer?>(null)
    val sectionsNewsState: StateFlow<NewsContainer?> = _sectionNewsState.asStateFlow()

    private val _selectedSectionState = MutableStateFlow(FIRST_SECTIONS_ID)
    val selectedSectionsState: StateFlow<String> = _selectedSectionState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _showSnackBarEvent = MutableSharedFlow<Event<Int>>()
    val showSnackBarEvent: SharedFlow<Event<Int>> = _showSnackBarEvent.asSharedFlow()


    init {
        fetchDataFromApi()
    }

    fun fetchDataFromApi(responseStatus: (Resource<Exception>) -> Unit = {}) {
        getSectionsFilteredById(DEFAULT_SECTIONS)
        getLatestNews(SectionWrapper(sectionName = LATEST_NEWS, value = ""), responseStatus)
        getNewsBySection {}
    }

    fun showSnackBarMessage(messageResId: Int) {
        viewModelScope.launch {
            _showSnackBarEvent.emit(Event(messageResId))
        }
    }

    fun setSelectedSections(section: String) {
        _selectedSectionState.value = section
    }

    fun getSectionsFilteredById(sections: List<String>? = null) {
        viewModelScope.launch(dispatchers.main) {
            _sectionsListState.value = if (sections != null) {
                newsUseCases.getSectionsUseCase(dispatchers.io).first().filter {
                    sections.contains(it.id)
                }
            } else {
                newsUseCases.getSectionsUseCase(dispatchers.io).first()
            }
        }
    }

    fun getLatestNews(section: SectionWrapper, responseStatus: (Resource<Exception>) -> Unit) {
        viewModelScope.launch(dispatchers.main) {
            _latestNewsState.value =
                newsUseCases.getNewsBySectionUseCase(dispatchers.io, section, responseStatus)
                    .first()
        }
    }

    fun getNewsBySection(responseStatus: (Resource<Exception>) -> Unit) {
        viewModelScope.launch(dispatchers.main) {
            val selectedSection = SectionWrapper(
                sectionName = selectedSectionsState.value,
                value = selectedSectionsState.value
            )
            _sectionNewsState.value =
                newsUseCases.getNewsBySectionUseCase(
                    dispatchers.io,
                    selectedSection,
                    responseStatus
                ).first()
        }
    }

    fun setShouldRefresh(value: Boolean) {
        _isRefreshing.value = value
    }
}