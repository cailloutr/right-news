package com.cailloutr.rightnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiStateViewModel : ViewModel() {

    var hasComponents = VisualComponents()
        set(value) {
            field = value
            _components.value = value
        }

    private var _components = MutableStateFlow(hasComponents)
    val components: StateFlow<VisualComponents> = _components.asStateFlow()
}

data class VisualComponents(
    val bottomNavigation: Boolean = false,
)