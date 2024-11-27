package com.example.eventify.common.utils

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
    return email.matches(emailRegex)
}

fun copyToClipboard(context: Context, text: String, message: String? = null) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)

    if (message != null) NancyToast.makeText(context, message, NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
}