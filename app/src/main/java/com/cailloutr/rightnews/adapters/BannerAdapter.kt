package com.cailloutr.rightnews.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.cailloutr.rightnews.databinding.LatestNewsBannerBinding
import com.cailloutr.rightnews.model.BannerNews

const val TAG = "BannerAdapter"

class BannerAdapter(
    private val onClick: (BannerNews) -> Unit,
) : ListAdapter<BannerNews, BannerAdapter.BannerViewHolder>(DiffCallback) {

    private lateinit var banner: BannerNews

    inner class BannerViewHolder(
        private val binding: LatestNewsBannerBinding,
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (::banner.isInitialized) {
                    onClick
                    Log.i(TAG, "Click: ")
                }
            }
        }

        fun bind(banner: BannerNews) {
            binding.apply {
                bannerNewsAuthor.text = banner.author
                bannerNewsTitle.text = banner.title
                bannerNewsDescription.text = banner.description
                bannerImage.load(bannerImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LatestNewsBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BannerNews>() {
            override fun areItemsTheSame(oldItem: BannerNews, newItem: BannerNews) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BannerNews, newItem: BannerNews) =
                oldItem == newItem
        }
    }
}