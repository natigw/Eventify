package com.example.common.utils.functions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun dateFormatter_RemoveDashes_DMYtoDMY(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val parsedDate = LocalDate.parse(inputDate, inputFormatter)
    val formattedDate = parsedDate.format(outputFormatter)
    return formattedDate
}

fun dateFormatter_RemoveDashes_YMDtoDMY(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Updated format
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val parsedDate = LocalDate.parse(inputDate, inputFormatter)
    val formattedDate = parsedDate.format(outputFormatter)
    return formattedDate
}

fun instantTimeConverter(inputTime: Instant): ZonedDateTime {
    val timeZone = ZoneId.systemDefault() //Get the system's default time zone
    val localTime = ZonedDateTime.ofInstant(inputTime, timeZone) //Convert Instant to ZonedDateTime
    return localTime
}

fun dateFormatterIFYEAR_MNAMED_Comma_HM(inputDate: Instant): String {
    val timeInZone = instantTimeConverter(inputDate)

    val currentYear = ZonedDateTime.now(ZoneId.systemDefault()).year
    val currentDay = ZonedDateTime.now(ZoneId.systemDefault()).dayOfYear

    val formattedDate = if (timeInZone.year != currentYear) {
        //format as "20 Jan 2023, 21:39"
        timeInZone.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
    } else if (timeInZone.dayOfYear != currentDay) {
        //format as "Jan 20, 21:39"
        timeInZone.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
    } else {
        //format as "21:39"
        timeInZone.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    return formattedDate
}