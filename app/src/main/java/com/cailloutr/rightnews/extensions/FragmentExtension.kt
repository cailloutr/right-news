package com.cailloutr.rightnews.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.appcompat.widget.Toolbar

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun Fragment.setupToolbar(toolbar: Toolbar) {
    val navController = findNavController()
    val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.newsFragment,
            R.id.favoritesFragment,
            R.id.profileFragment
        )
    )

    toolbar.setupWithNavController(navController, appBarConfiguration)
}
