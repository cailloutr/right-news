package com.cailloutr.rightnews.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cailloutr.rightnews.databinding.SectionsContentItemBinding
import com.cailloutr.rightnews.model.AllSectionsItem
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.recyclerview.viewholder.HeadlineViewHolder

class HeadlineAdapter(
    private val onClick: (Section) -> Unit
) : ListAdapter<AllSectionsItem, HeadlineViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        return HeadlineViewHolder(
            SectionsContentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<AllSectionsItem>() {
            override fun areItemsTheSame(
                oldItem: AllSectionsItem,
                newItem: AllSectionsItem,
            ) = oldItem.index == newItem.index


            override fun areContentsTheSame(
                oldItem: AllSectionsItem,
                newItem: AllSectionsItem,
            ) = oldItem == newItem

        }
    }
}