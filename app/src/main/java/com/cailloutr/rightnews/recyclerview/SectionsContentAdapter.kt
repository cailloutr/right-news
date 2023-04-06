package com.cailloutr.rightnews.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.cailloutr.rightnews.databinding.ChipItemBinding
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.recyclerview.viewholder.SectionsContentViewHolder

class SectionsContentAdapter(
    private val list: List<Section>
) : Adapter<SectionsContentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsContentViewHolder {
        return SectionsContentViewHolder(
            ChipItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: SectionsContentViewHolder, position: Int) {
        holder.bind(list[position])
    }
}