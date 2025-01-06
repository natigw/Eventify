package com.example.common.utils

import android.content.Context

fun nancyToast(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT,
    @NancyToast.LayoutType type: Int = NancyToast.DEFAULT
) {
    NancyToast.makeText(context, message, duration, type).show()
}

fun nancyToastSuccess(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.SUCCESS).show()
}

fun nancyToastWarning(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.WARNING).show()
}

fun nancyToastError(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.ERROR).show()
}

fun nancyToastInfo(
    context: Context,
    message: String?,
    @NancyToast.Duration duration: Int = NancyToast.LENGTH_SHORT
) {
    NancyToast.makeText(context, message, duration, NancyToast.INFO).show()
}