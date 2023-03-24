package com.cailloutr.rightnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cailloutr.rightnews.adapters.viewholder.BannerViewHolder
import com.cailloutr.rightnews.adapters.viewholder.BaseViewHolder
import com.cailloutr.rightnews.adapters.viewholder.NewsViewHolder
import com.cailloutr.rightnews.databinding.CategorizedNewsItemBinding
import com.cailloutr.rightnews.databinding.LatestNewsBannerBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.model.BannerNews

const val TAG = "BannerAdapter"

class BannerAdapter(
    private val itemNewsType: ItemNewsType,
    private val onClick: (BannerNews) -> Unit,
) : ListAdapter<BannerNews, BaseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (itemNewsType == ItemNewsType.BANNER)
            BannerViewHolder(
                LatestNewsBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClick
            ) else
            NewsViewHolder(
                CategorizedNewsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClick
            )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BannerNews>() {
            override fun areItemsTheSame(oldItem: BannerNews, newItem: BannerNews) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BannerNews, newItem: BannerNews) =
                oldItem == newItem
        }
    }
}