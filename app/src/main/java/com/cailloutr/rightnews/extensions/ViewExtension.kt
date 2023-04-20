package com.cailloutr.rightnews.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
    message,
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

fun View.animateScale() {
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.1f)
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1f)

    val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
    animator.repeatCount = 1
    animator.repeatMode = ObjectAnimator.REVERSE
    animator.start()
}

fun View.animateScaleBack() {
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)

    val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
    animator.start()
}
