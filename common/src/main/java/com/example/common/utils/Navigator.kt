package com.example.common.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.common.R

fun NavController.navigateWithAnimationLeftToRight(
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_left)
        setExitAnim(R.anim.to_right)
        setPopEnterAnim(R.anim.from_right)
        setPopExitAnim(R.anim.to_left)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, navOptions)
}

fun NavController.navigateWithAnimationRightToLeft(
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_right)
        setExitAnim(R.anim.to_left)
        setPopEnterAnim(R.anim.from_left)
        setPopExitAnim(R.anim.to_right)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, navOptions)
}

fun NavController.navigateWithoutAnimation(
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, navOptions)
}