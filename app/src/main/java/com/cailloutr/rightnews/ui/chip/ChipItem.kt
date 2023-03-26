package com.cailloutr.rightnews.ui.chip

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cailloutr.rightnews.R
import com.google.android.material.chip.Chip


private const val TAG = "ChipItem"

data class ChipItem(
    val id: String,
    val text: String,
    val isChecked: Boolean = false
)

fun ChipItem.toChip(context: Context, viewGroup: ViewGroup): Chip {
    val chip = LayoutInflater.from(context).inflate(R.layout.chip_item, viewGroup, false) as Chip

    chip.text = text
    chip.id = View.generateViewId()
    chip.isChecked = isChecked

    chip.setOnClickListener {
        Log.i(TAG, "Chip ${chip.id} clicked")
    }

    return chip
}