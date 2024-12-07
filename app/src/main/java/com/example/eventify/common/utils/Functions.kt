package com.example.eventify.common.utils

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random

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

fun numberFormatter(count: Long): String {
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", count / 1000.0.pow(exp.toDouble()), "kMBTP"[exp - 1])
}

fun numberFormatterSpaced(count: Long): String {
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    val value = count / 1000.0.pow(exp.toDouble())
    return String.format("%.1f%c", value, "KMBTP"[exp - 1])
}

fun randomInteger(min: Int, max: Int): Int {
    return Random.nextInt(min, max)
}

fun randomDouble(min: Double = 0.0, max: Double): Double {
    return Random.nextDouble(min, max)
}

fun roundDouble(number: Double, toStep: Int = 2): Double {
    val rounded = Math.round(number * 10.0.pow(toStep)) / 10.0.pow(toStep)
    return rounded
}