package com.cailloutr.rightnews.extensions

import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun Toolbar.handleSystemBarOverlaps() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updatePadding(
            top = insets.top
        )

        WindowInsetsCompat.CONSUMED
    }
}