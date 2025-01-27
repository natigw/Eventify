package com.example.common.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(rootView: View) {
    val inputMethodManager = rootView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(rootView.windowToken, 0)
}

fun showKeyboard(rootView: View) {
    val inputMethodManager = rootView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(rootView, InputMethodManager.SHOW_IMPLICIT)
}