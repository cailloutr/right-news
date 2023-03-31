package com.cailloutr.rightnews.ui

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.cailloutr.rightnews.R

class CustomItemAnimator: DefaultItemAnimator() {
    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo,
    ): Boolean {
        viewHolder.itemView.animation = AnimationUtils.loadAnimation(
            viewHolder.itemView.context,
            R.anim.scale
        )
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo)
    }
}