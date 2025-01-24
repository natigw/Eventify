package com.example.common.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.common.R

fun navigateWithAnimationLeftToRight(
    navController: NavController,
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_left)
        setExitAnim(R.anim.to_right)
        setPopEnterAnim(R.anim.fade_in)
        setPopExitAnim(R.anim.fade_out)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    navController.navigate(destination, navOptions)
}

fun navigateWithAnimationRightToLeft(
    navController: NavController,
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.from_right)
        setExitAnim(R.anim.to_left)
        setPopEnterAnim(R.anim.fade_in)
        setPopExitAnim(R.anim.fade_out)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    navController.navigate(destination, navOptions)
}

fun navigateWithoutAnimation(
    navController: NavController,
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    navController.navigate(destination, navOptions)
}