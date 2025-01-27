package com.example.common.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.common.R

fun navigateWithAnimationFade(
    navController: NavController,
    destination: Int,
    popUpTo: Int? = null,
    inclusive: Boolean = true
) {
    val navOptions = NavOptions.Builder().apply {
        setEnterAnim(R.anim.fade_in)
        setExitAnim(R.anim.fade_out)
        popUpTo?.let { setPopUpTo(it, inclusive) }
    }.build()

    navController.navigate(destination, null, navOptions)
}