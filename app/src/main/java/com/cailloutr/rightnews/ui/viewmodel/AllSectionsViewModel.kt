package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val TAG = "AllSectionsViewModel"

@HiltViewModel
class AllSectionsViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    init {
        getAllSections()
    }

    fun getAllSections() = newsUseCases.getSectionsUseCase(dispatchers.io)
}