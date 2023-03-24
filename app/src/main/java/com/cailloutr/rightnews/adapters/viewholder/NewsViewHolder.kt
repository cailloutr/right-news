package com.cailloutr.rightnews.adapters.viewholder

import coil.load
import com.cailloutr.rightnews.databinding.CategorizedNewsItemBinding
import com.cailloutr.rightnews.model.BannerNews

class NewsViewHolder(
    private val binding: CategorizedNewsItemBinding,
    onClick: (BannerNews) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    override fun bind(bannerNews: BannerNews) {
        super.bind(bannerNews)
        binding.apply {
            newsAuthor.text = bannerNews.author
            newsTitle.text = bannerNews.title
            newsDate.text = bannerNews.description
            newsImage.load(bannerNews.image) {
                crossfade(true)
            }
        }
    }
}