package com.cailloutr.rightnews.adapters.viewholder

import coil.load
import com.cailloutr.rightnews.databinding.CategorizedNewsItemBinding
import com.cailloutr.rightnews.model.News

class NewsViewHolder(
    private val binding: CategorizedNewsItemBinding,
    onClick: (News) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    override fun bind(news: News) {
        super.bind(news)
        binding.apply {
            newsAuthor.text = news.sectionName
            newsTitle.text = news.webTitle
            newsDate.text = news.webPublicationDate
            newsImage.load(news.thumbnail) {
                crossfade(true)
            }
        }
    }
}