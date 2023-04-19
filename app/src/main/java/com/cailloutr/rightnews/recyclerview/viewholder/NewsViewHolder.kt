package com.cailloutr.rightnews.recyclerview.viewholder

import android.os.Build
import androidx.annotation.RequiresApi
import coil.load
import com.cailloutr.rightnews.databinding.CategorizedNewsItemBinding
import com.cailloutr.rightnews.model.Article
import com.cailloutr.rightnews.util.DateUtil

class NewsViewHolder(
    private val binding: CategorizedNewsItemBinding,
    onClick: (Article) -> Unit,
) : BaseViewHolder(binding.root, onClick) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(article: Article) {
        super.bind(article)
        binding.apply {
            newsPillarName.text = article.pillarName
            newsTitle.text = article.webTitle
            newsDate.text = DateUtil.getFormattedDate(article.webPublicationDate)
            newsImage.load(article.thumbnail) {
                crossfade(true)
            }
        }
    }
}