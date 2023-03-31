package com.cailloutr.rightnews.recyclerview.viewholder

import android.os.Build
import androidx.annotation.RequiresApi
import coil.load
import com.cailloutr.rightnews.databinding.CategorizedNewsItemBinding
import com.cailloutr.rightnews.model.News
import com.cailloutr.rightnews.util.DateUtil

class NewsViewHolder(
    private val binding: CategorizedNewsItemBinding,
    onClick: (News) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(news: News) {
        super.bind(news)
        binding.apply {
            newsPillarName.text = news.pillarName
            newsTitle.text = news.webTitle
            newsDate.text = DateUtil.getFormattedDate(news.webPublicationDate)
            newsImage.load(news.thumbnail) {
                crossfade(true)
            }
        }
    }
}