package com.cailloutr.rightnews.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

fun View.hide() {
    this.animate()
        .alpha(0.0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@hide.clearAnimation()
                this@hide.visibility = View.GONE
            }
        })
}

fun View.show() {
    this.animate()
        .alpha(1f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@show.clearAnimation()
                this@show.visibility = View.VISIBLE
            }
        })
}

fun View.snackbar(message: String) = Snackbar.make(
    this,
    "Error: ${message}",
    Snackbar.LENGTH_SHORT
).show()

/**
 * Update padding in content based In CardView Height
 * */
fun View.setupPaddingInView(parent: ViewGroup, target: View) {
    val viewTreeObserver = parent.viewTreeObserver
    viewTreeObserver.addOnGlobalLayoutListener {
        val height = target.measuredHeight
        this.setPadding(0, height / 2 + 30, 0, 0)
    }
}
