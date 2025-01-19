package com.example.common.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun crossfadeAppear(view: View, duration: Long = 500) {
    view.alpha = 0f
    view.visibility = View.VISIBLE
    view.animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(null)  //clear any animation listener
}

fun crossfadeDisappear(view: View, duration: Long = 500, isGone: Boolean = true) {
    view.animate()
        .alpha(0f)
        .setDuration(duration)
        //After the animation ends, set its visibility to GONE as an optimization step so it doesn't participate in layout passes.
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = if (isGone) View.GONE else View.INVISIBLE
            }
        })
}