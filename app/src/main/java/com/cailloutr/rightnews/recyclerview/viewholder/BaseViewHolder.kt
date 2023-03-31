package com.cailloutr.rightnews.recyclerview.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cailloutr.rightnews.recyclerview.TAG
import com.cailloutr.rightnews.model.News

open class BaseViewHolder(
    view: View,
    private val onClick: (News) -> Unit,
) : ViewHolder(view) {

    private lateinit var news: News

    open fun bind(news: News) {
        this.news = news
    }

    init {
        itemView.setOnClickListener {
            onClick(news)
            Log.i(TAG, "Click: ")
        }
    }
}