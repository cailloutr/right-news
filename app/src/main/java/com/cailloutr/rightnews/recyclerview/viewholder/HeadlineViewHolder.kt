package com.cailloutr.rightnews.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cailloutr.rightnews.databinding.SectionsContentItemBinding
import com.cailloutr.rightnews.model.AllSectionsItem
import com.cailloutr.rightnews.recyclerview.SectionsContentAdapter

class HeadlineViewHolder(
    private val binding: SectionsContentItemBinding,
    private val onClick: (String) -> Unit
) : ViewHolder(binding.root) {

    private lateinit var section: AllSectionsItem

    fun bind(section: AllSectionsItem) {
        binding.sectionsIndex.text = section.index

        val adapter = SectionsContentAdapter(section.list) {
            onClick(it)
        }

        binding.sectionsRecyclerview.adapter = adapter
        val spanCount = when (section.list.size) {
            in 1..4 -> {
                1
            }
            in 5..8 -> {
                2
            }
            else -> {
                3
            }
        }
        binding.sectionsRecyclerview.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
}