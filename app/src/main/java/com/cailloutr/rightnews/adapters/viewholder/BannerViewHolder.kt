package com.cailloutr.rightnews.adapters.viewholder

import coil.load
import com.cailloutr.rightnews.databinding.LatestNewsBannerBinding
import com.cailloutr.rightnews.model.BannerNews

class BannerViewHolder(
    private val binding: LatestNewsBannerBinding,
    onClick: (BannerNews) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    override fun bind(bannerNews: BannerNews) {
        super.bind(bannerNews)
        binding.apply {
            bannerNewsAuthor.text = bannerNews.author
            bannerNewsTitle.text = bannerNews.title
            bannerNewsDescription.text = bannerNews.description
            bannerNewsImage.load(bannerNews.image) {
                crossfade(true)
            }
        }
    }
}