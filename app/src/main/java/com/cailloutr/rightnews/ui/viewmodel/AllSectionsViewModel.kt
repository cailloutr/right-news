package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "AllSectionsViewModel"

@HiltViewModel
class AllSectionsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    private val _allSectionsState = MutableStateFlow<List<RoomSection>>(listOf())
    val allSectionsState: StateFlow<List<RoomSection>> = _allSectionsState.asStateFlow()

    init {
        getAllSections()
    }

    fun getAllSections() {
        viewModelScope.launch(dispatchers.main) {
            _allSectionsState.value = newsUseCases.getSectionsUseCase(dispatchers.io).first()
        }
    }
}