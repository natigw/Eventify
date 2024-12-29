package com.example.common.utils.functions

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun dateFormatterDMYtoDMY(inputDate: String): String {
    var formattedDate = inputDate
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")


        val parsedDate = LocalDate.parse(inputDate, inputFormatter)
        formattedDate = parsedDate.format(outputFormatter)
    }
    return formattedDate
}

fun dateFormatterYMDtoDMY(inputDate: String): String {
    var formattedDate = inputDate
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Updated format
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        val parsedDate = LocalDate.parse(inputDate, inputFormatter)
        formattedDate = parsedDate.format(outputFormatter)
    }
    return formattedDate
}
