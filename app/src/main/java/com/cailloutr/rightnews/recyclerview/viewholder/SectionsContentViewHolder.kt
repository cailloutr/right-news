package com.cailloutr.rightnews.recyclerview.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.databinding.ChipItemBinding
import com.cailloutr.rightnews.extensions.animateScale
import com.cailloutr.rightnews.extensions.animateScaleBack
import com.cailloutr.rightnews.model.Section
import com.google.android.material.chip.Chip

class SectionsContentViewHolder(
    private val binding: ChipItemBinding,
    onclick: (Section) -> Unit
) : ViewHolder(binding.root) {

    private lateinit var section: Section

    init {
        itemView.setOnClickListener {
            if (!((it) as Chip).isChecked) {
                it.animateScaleBack()
            } else {
                it.animateScale()
            }
            onclick(section)
        }
    }

    fun bind(section: Section) {
        this.section = section
        binding.root.apply {
            text = section.title
            chipStrokeColor = ContextCompat.getColorStateList(
                binding.root.context,
                R.color.all_sections_chip_selector
            )
        }
    }
}