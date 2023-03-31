package com.cailloutr.rightnews.recyclerview.viewholder

import android.text.Html
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
            bannerNewsDescription.text = Html.fromHtml(news.trailText, Html.FROM_HTML_MODE_COMPACT)
            bannerNewsImage.load(news.thumbnail) {
                crossfade(true)
            }
        }
    }
}