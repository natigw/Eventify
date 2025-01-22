package com.example.common.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.common.R

fun NavController.navigateWithAnimationLeftToRight(
    destination: Int,
    args: Bundle? = null,
    popUpTo: Int? = null,
    inclusive: Boolean = false
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_left)
        setExitAnim(R.anim.to_right)
        setPopEnterAnim(R.anim.from_right)
        setPopExitAnim(R.anim.to_left)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, args, navOptions)
}

fun NavController.navigateWithAnimationRightToLeft(
    destination: Int,
    args: Bundle? = null,
    popUpTo: Int? = null,
    inclusive: Boolean = false
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_right)
        setExitAnim(R.anim.to_left)
        setPopEnterAnim(R.anim.from_left)
        setPopExitAnim(R.anim.to_right)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, args, navOptions)
}

fun NavController.navigateWithoutAnimation(
    destination: Int,
    args: Bundle? = null,
    popUpTo: Int? = null,
    inclusive: Boolean = false
) {
    val navOptions = NavOptions.Builder().apply {
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    this.navigate(destination, args, navOptions)
}