package com.example.common.utils

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

fun startShimmer(shimmerView: ShimmerFrameLayout) {
    shimmerView.startShimmer()
    shimmerView.visibility = View.VISIBLE
}

fun stopShimmer(shimmerView: ShimmerFrameLayout) {
    shimmerView.stopShimmer()
    shimmerView.visibility = View.INVISIBLE
}

fun stopShimmerGone(shimmerView: ShimmerFrameLayout) {
    shimmerView.stopShimmer()
    shimmerView.visibility = View.GONE
}