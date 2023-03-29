package com.cailloutr.rightnews.adapters.viewholder

import coil.load
import com.cailloutr.rightnews.databinding.LatestNewsBannerBinding
import com.cailloutr.rightnews.model.News

class BannerViewHolder(
    private val binding: LatestNewsBannerBinding,
    onClick: (News) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    override fun bind(news: News) {
        super.bind(news)
        binding.apply {
            bannerNewsTitle.text = news.webTitle
            bannerNewsDescription.text = news.trailText
            bannerNewsImage.load(news.thumbnail) {
                crossfade(true)
            }
        }
    }
}