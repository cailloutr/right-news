package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.model.Sections
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
class AllSectionsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    private var _sectionsListState = MutableStateFlow<Resource<Sections>>(Resource.loading(data = null))
    val sectionsListState: StateFlow<Resource<Sections>> = _sectionsListState.asStateFlow()

    init {
        getAllSections()
    }

    fun getAllSections() {
        viewModelScope.launch(dispatchers.main) {
            _sectionsListState.value = newsUseCases.getSectionsUseCase()
        }
    }
}