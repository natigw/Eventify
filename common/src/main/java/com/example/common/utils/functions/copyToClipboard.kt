package com.example.common.utils.functions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.common.utils.nancyToastSuccess

fun copyToClipboard(context: Context, text: String, message: String? = null) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)

    if (message != null) nancyToastSuccess(context, message)
}