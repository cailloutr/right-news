package com.cailloutr.rightnews.recyclerview.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cailloutr.rightnews.recyclerview.TAG
import com.cailloutr.rightnews.model.Article

open class BaseViewHolder(
    view: View,
    private val onClick: (Article) -> Unit,
) : ViewHolder(view) {

    private lateinit var article: Article

    open fun bind(article: Article) {
        this.article = article
    }

    init {
        itemView.setOnClickListener {
            onClick(article)
            Log.i(TAG, "Click: $article")
        }
    }
}