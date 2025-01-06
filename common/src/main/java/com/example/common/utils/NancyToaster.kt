package com.example.common.utils

import android.content.Context

fun nancyToast(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT,
    @NancyToast.LayoutType type: Int = NancyToast.DEFAULT
) {
    NancyToast.makeText(context, message, duration, type, false).show()
}

fun nancyToastSuccess(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.SUCCESS, false).show()
}

fun nancyToastWarning(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.WARNING, false).show()
}

fun nancyToastError(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.ERROR, false).show()
}

fun nancyToastInfo(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.INFO, false).show()
}