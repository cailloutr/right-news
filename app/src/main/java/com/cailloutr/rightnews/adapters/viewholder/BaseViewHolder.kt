package com.cailloutr.rightnews.adapters.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cailloutr.rightnews.adapters.TAG
import com.cailloutr.rightnews.model.BannerNews

open class BaseViewHolder(
    view: View,
    private val onClick: (BannerNews) -> Unit,
) : ViewHolder(view) {

    private lateinit var bannerNews: BannerNews

    open fun bind(bannerNews: BannerNews) {
        this.bannerNews = bannerNews
    }

    init {
        itemView.setOnClickListener {
            onClick(bannerNews)
            Log.i(TAG, "Click: ")
        }
    }
}