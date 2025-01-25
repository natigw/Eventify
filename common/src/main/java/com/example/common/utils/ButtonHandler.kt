package com.example.common.utils

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ProgressBar

fun blockButton(progressBar: ProgressBar, button: Button) {
    progressBar.visibility = View.VISIBLE
    button.apply {
        isEnabled = false
        text = null
        setBackgroundColor(Color.parseColor("#FFDADADA")) //button_disabled
    }
}

fun resetButton(progressBar: ProgressBar, button: Button, buttonText: String, buttonColor: Int) {
    progressBar.visibility = View.INVISIBLE
    button.apply {
        isEnabled = true
        text = buttonText
        setBackgroundColor(buttonColor)
    }
}