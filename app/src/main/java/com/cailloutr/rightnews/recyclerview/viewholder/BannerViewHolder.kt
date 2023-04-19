package com.cailloutr.rightnews.recyclerview.viewholder

import android.text.Html
import coil.load
import com.cailloutr.rightnews.databinding.LatestNewsBannerBinding
import com.cailloutr.rightnews.model.Article

class BannerViewHolder(
    private val binding: LatestNewsBannerBinding,
    onClick: (Article) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    override fun bind(article: Article) {
        super.bind(article)
        binding.apply {
            bannerNewsTitle.text = article.webTitle
            bannerNewsDescription.text = Html.fromHtml(article.trailText, Html.FROM_HTML_MODE_COMPACT)
            bannerNewsImage.load(article.thumbnail) {
                crossfade(true)
            }
        }
    }
}